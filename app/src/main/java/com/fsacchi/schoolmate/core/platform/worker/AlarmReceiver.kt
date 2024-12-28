package com.fsacchi.schoolmate.core.platform.worker

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarme disparado!")

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val jobInfo = JobInfo.Builder(
            1,
            ComponentName(context, JobNotificationService::class.java)
        )
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(false)
            .setOverrideDeadline(0)
            .build()

        jobScheduler.schedule(jobInfo)

        scheduleDailyAlarm(context)
    }
}