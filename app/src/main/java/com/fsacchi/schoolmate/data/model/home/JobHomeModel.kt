package com.fsacchi.schoolmate.data.model.home

import android.os.Parcelable
import com.fsacchi.schoolmate.core.extensions.formatDateExtensive
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.data.model.job.JobModel
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class JobHomeModel(
    val dateSelected: Date? = now(),
    val jobsToday: List<JobModel> = arrayListOf(),
    val nextJobs: List<JobModel> = arrayListOf()
): Parcelable {
    fun getDateExtensive() = dateSelected?.formatDateExtensive()
}