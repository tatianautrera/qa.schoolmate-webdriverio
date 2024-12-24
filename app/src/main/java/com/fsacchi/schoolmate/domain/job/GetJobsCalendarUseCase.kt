package com.fsacchi.schoolmate.domain.job

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.data.model.home.JobCalendarModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.data.model.job.TypeJob
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.JobCalendarUiState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class GetJobsCalendarUseCase(
    private val db: FirebaseFirestore
) : UseCase<GetJobsCalendarUseCase.Params, JobCalendarUiState>() {

    override suspend fun execute(param: Params): Flow<JobCalendarUiState> = flow {
        emit(JobCalendarUiState(JobCalendarUiState.ScreenType.Await))
        val result = getJobs(param.userUid)
        emit(result)

    }

    data class Params(val userUid: String)

    private suspend fun getJobs(
        userUid: String
    ): JobCalendarUiState {
        return try {
            val querySnapshot = getJobsQuery(userUid)
                .get().await()
            val groupedJobs = querySnapshot.documents.mapNotNull { doc ->
                createJob(doc)
            }.groupBy { it.dtJob }

            val listJobsCalendar = groupedJobs.map { (date, jobs) ->
                JobCalendarModel(
                    dateSelected = date ?: now(),
                    jobsToday = jobs
                )
            }

            JobCalendarUiState(
                screenType = JobCalendarUiState.ScreenType.Loaded(listJobsCalendar)
            )
        } catch (e: Exception) {
            JobCalendarUiState(
                screenType = JobCalendarUiState.ScreenType.Error(
                    errorTitle = "Erro ao carregar atividades",
                    errorMessage = e.handleFirebaseErrors()
                )
            )
        }
    }

    private fun createJob(doc: DocumentSnapshot): JobModel? {
        return try {
            JobModel(
                id = doc.id,
                disciplineId = doc.getString("disciplineId").orEmpty(),
                typeJob = TypeJob.valueOf(doc.getString("jobType").orEmpty()),
                descrTypeJob = TypeJob.valueOf(doc.getString("jobType").orEmpty()).message,
                dtJob = doc.getTimestamp("dtDelivery")?.toDate(),
                observation = doc.getString("observation").orEmpty(),
                status = doc.getString("status").orEmpty()
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun getJobsQuery(userUid: String): Query {
        return db.collection("users")
            .document(userUid)
            .collection("jobs")
    }
}
