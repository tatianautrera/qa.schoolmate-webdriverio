package com.fsacchi.schoolmate.core.platform.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Random

class JobNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams), KoinComponent {

    companion object {
        const val CHANNEL_ID = "school_mate_channel"
        const val CHANNEL_NAME = "School Mate Notifications"
    }

    private val getUserUseCase: GetUserUseCase by inject()
    private val getJobNotificationUseCase: GetJobNotificationUseCase by inject()
    private val homeViewModel: HomeViewModel by inject()

    private val job = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun doWork(): Result {
        job.launch {
            val configModel = homeViewModel.getConfigNotification()

            if (configModel.allowNotification) {
                getUserUseCase().collectLatest {
                    if (it.screenType is UserUiState.ScreenType.Loaded) {
                        val userUid = it.screenType.userEntity?.uid
                        userUid?.let { uid ->
                            getJobNotificationUseCase(uid).collectLatest { jobNotificationUiState ->
                                if (jobNotificationUiState.screenType is JobNotificationUiState.ScreenType.Loaded) {
                                    val jobModels = jobNotificationUiState.screenType.jobs
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
        }

        return Result.success()
    }

    private fun sendNotification(jobModel: JobModel) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Avisos de atividades a serem entregues"
        }
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, HomeActivity::class.java).apply {
            putExtra("jobModel", jobModel)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            Random().nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = android.app.Notification.Builder(applicationContext, channel.id)
            .setContentTitle(jobModel.dateToDelivery())
            .setContentText(jobModel.messageNotificationJob())
            .setSmallIcon(R.drawable.ic_check)
            .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Random().nextInt(), notification)
    }
}
