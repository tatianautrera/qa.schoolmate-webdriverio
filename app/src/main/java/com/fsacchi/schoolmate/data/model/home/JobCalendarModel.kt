package com.fsacchi.schoolmate.data.model.home

import android.os.Parcelable
import com.fsacchi.schoolmate.core.extensions.formatDateExtensive
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.data.model.job.JobModel
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class JobCalendarModel(
    val dateSelected: Date = now(),
    var jobsToday: List<JobModel> = arrayListOf()
): Parcelable