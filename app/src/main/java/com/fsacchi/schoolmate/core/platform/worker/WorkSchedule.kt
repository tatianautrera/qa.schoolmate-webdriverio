package com.fsacchi.schoolmate.core.platform.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyJob(context: Context) {
    val currentTime = Calendar.getInstance()
    val targetTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 2)
        set(Calendar.MINUTE, 40)
        set(Calendar.SECOND, 0)
    }

    val delay = if (currentTime.before(targetTime)) {
        targetTime.timeInMillis - currentTime.timeInMillis
    } else {
        targetTime.timeInMillis + TimeUnit.DAYS.toMillis(1) - currentTime.timeInMillis
    }

    val workRequest = OneTimeWorkRequestBuilder<JobNotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "DailyJobNotification",
        ExistingWorkPolicy.REPLACE,
        workRequest
    )
}