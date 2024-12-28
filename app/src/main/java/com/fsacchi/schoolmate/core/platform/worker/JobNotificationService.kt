package com.fsacchi.schoolmate.core.platform.worker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.daysBetweenNow
import com.fsacchi.schoolmate.core.features.home.HomeActivity
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.data.model.job.TypeJob
import com.fsacchi.schoolmate.domain.home.GetUserUseCase
import com.fsacchi.schoolmate.domain.job.GetJobNotificationUseCase
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.JobNotificationUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Random
import java.util.concurrent.CancellationException

@SuppressLint("SpecifyJobSchedulerIdRange")
class JobNotificationService : JobService(), KoinComponent {

    companion object {
        const val CHANNEL_ID = "school_mate_channel"
        const val CHANNEL_NAME = "School Mate Notifications"
        const val TAG = "JobNotificationService"
    }

    private val getUserUseCase: GetUserUseCase by inject()
    private val getJobNotificationUseCase: GetJobNotificationUseCase by inject()
    private val homeViewModel: HomeViewModel by inject()

    private var job: Job? = null

    override fun onStartJob(params: JobParameters?): Boolean {
        if (job?.isActive == true) {
            Log.d(TAG, "Job já está em execução, ignorando novo agendamento.")
            return false
        }

        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val configModel = homeViewModel.getConfigNotification()

                if (configModel.allowNotification) {
                    getUserUseCase().collect { userState ->
                        Log.d(TAG, "Estado atual do usuário: ${userState.screenType}")

                        if (userState.screenType is UserUiState.ScreenType.Loaded) {
                            Log.d(TAG, "Usuário carregado: ${userState.screenType.userEntity?.uid}")

                            val userUid = userState.screenType.userEntity?.uid
                            userUid?.let { uid ->
                                getJobNotificationUseCase(uid).collect { jobNotificationState ->
                                    Log.d(TAG, "Estado atual dos jobs: ${jobNotificationState.screenType}")

                                    if (jobNotificationState.screenType is JobNotificationUiState.ScreenType.Loaded) {
                                        Log.d(TAG, "Jobs carregados: ${jobNotificationState.screenType.jobs.size}")

                                        val jobModels = jobNotificationState.screenType.jobs
                                        jobModels.forEach { jobModel ->
                                            val daysToDelivery = jobModel.dtJob?.daysBetweenNow()?.or(1) ?: return@forEach

                                            val daysUntil = when (jobModel.typeJob) {
                                                TypeJob.Job -> configModel.daysUntilJob.toInt()
                                                TypeJob.Test -> configModel.daysUntilTest.toInt()
                                                else -> configModel.daysUntilHomeWork.toInt()
                                            }

                                            if (daysToDelivery <= daysUntil) {
                                                sendNotification(jobModel)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao executar Job ${e.message}")
            } finally {
                jobFinished(params, false)
            }
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        job?.cancel(CancellationException("Job foi interrompido pelo sistema"))
        return true
    }

    private fun sendNotification(jobModel: JobModel) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Avisos de atividades a serem entregues"
        }
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("jobModel", jobModel)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            Random().nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(this, channel.id)
            .setContentTitle(jobModel.dateToDelivery())
            .setContentText(jobModel.messageNotificationJob())
            .setSmallIcon(R.drawable.ic_check)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        Log.d(TAG, "Notificação enviada")
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

