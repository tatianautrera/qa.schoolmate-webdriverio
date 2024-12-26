package com.fsacchi.schoolmate.core.platform.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.domain.home.GetUserUseCase
import com.fsacchi.schoolmate.domain.job.GetJobNotificationUseCase
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

    private val job = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun doWork(): Result {
        job.launch {
            getUserUseCase().collectLatest {
                if (it.screenType is UserUiState.ScreenType.Loaded) {
                    val userUid = it.screenType.userEntity?.uid
                    userUid?.let { uid ->
                        getJobNotificationUseCase(uid).collectLatest { jobNotificationUiState ->
                            if (jobNotificationUiState.screenType is JobNotificationUiState.ScreenType.Loaded) {
                                val jobModels = jobNotificationUiState.screenType.jobs
                                jobModels.forEach { jobModel ->
                                     sendNotification(jobModel)
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

        val notification = android.app.Notification.Builder(applicationContext, channel.id)
            .setContentTitle("School Mate")
            .setContentText(jobModel.messageNotificationJob())
            .setSmallIcon(R.drawable.ic_logo_notification)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random().nextInt(), notification)
    }
}
