package com.fsacchi.schoolmate.core.platform.worker

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
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
    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    jobScheduler.cancel(123)

    val delay = getInitialDelayForNextExecution()
    Log.d("JobNotificationService", "Delay calculado: $delay ms")

    val jobInfo = JobInfo.Builder(123, ComponentName(context, JobNotificationService::class.java))
        .setMinimumLatency(delay)
        .setOverrideDeadline(delay + TimeUnit.MINUTES.toMillis(15))
        .setPersisted(true)
        .setRequiresBatteryNotLow(true)
        .setRequiresCharging(false)
        .setRequiresDeviceIdle(false)
        .build()

    jobScheduler.schedule(jobInfo)
    Log.d("JobNotificationService", "Serviço agendado para execucao")
}

fun getInitialDelayForNextExecution(): Long {
    val now = Calendar.getInstance()
    val nextCall = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 18)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        if (timeInMillis <= now.timeInMillis) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    return nextCall.timeInMillis - now.timeInMillis
}

fun checkAndScheduleJob(context: Context) {
    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    val existingJob = jobScheduler.getPendingJob(123)

    if (existingJob == null) {
        scheduleDailyJob(context)
    }
}