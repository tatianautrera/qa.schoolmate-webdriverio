package com.fsacchi.schoolmate.core.platform.worker

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fsacchi.schoolmate.core.extensions.DateMasks.appFormat
import com.fsacchi.schoolmate.core.extensions.DateMasks.hourFormat
import com.fsacchi.schoolmate.core.extensions.formatWithUTC
import com.fsacchi.schoolmate.core.extensions.now
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyJob(context: Context) {
    val currentTime = Calendar.getInstance().apply {
        time = now()
    }

    val targetTime = Calendar.getInstance().apply {
        time = now()
        set(Calendar.HOUR_OF_DAY, 20)
        set(Calendar.MINUTE, 29)
        set(Calendar.SECOND, 0)
    }

    val delay = if (currentTime.before(targetTime)) {
        targetTime.timeInMillis - currentTime.timeInMillis
    } else {
        targetTime.timeInMillis + TimeUnit.DAYS.toMillis(1) - currentTime.timeInMillis
    }

    val workRequest = PeriodicWorkRequestBuilder<JobNotificationWorker>(15, TimeUnit.MINUTES)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.HOURS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "DailyJobNotification",
        ExistingPeriodicWorkPolicy.UPDATE,
        workRequest
    )
}