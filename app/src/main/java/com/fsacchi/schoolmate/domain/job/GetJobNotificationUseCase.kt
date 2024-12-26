package com.fsacchi.schoolmate.domain.job

import com.fsacchi.schoolmate.core.extensions.addDays
import com.fsacchi.schoolmate.core.extensions.endTime
import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.core.extensions.isToday
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.core.extensions.resetTime
import com.fsacchi.schoolmate.core.extensions.toDate
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.home.JobHomeModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.data.model.job.TypeJob
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.domain.discipline.GetDisciplinesUseCase
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.fsacchi.schoolmate.presentation.states.JobCalendarUiState
import com.fsacchi.schoolmate.presentation.states.JobHomeUiState
import com.fsacchi.schoolmate.presentation.states.JobNotificationUiState
import com.fsacchi.schoolmate.presentation.states.JobUiState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.sql.Timestamp
import java.util.Calendar
import java.util.Date
import kotlin.coroutines.resume

class GetJobNotificationUseCase(
    private val db: FirebaseFirestore,
    private val getDisciplinesUseCase: GetDisciplinesUseCase
) : UseCase<String, JobNotificationUiState>() {

    override suspend fun execute(param: String): Flow<JobNotificationUiState> = flow {
        emit(JobNotificationUiState(JobNotificationUiState.ScreenType.Await))
        getDisciplinesUseCase.execute(param).collect {
            when(it.screenType) {
                DisciplineUiState.ScreenType.Await -> {}
                is DisciplineUiState.ScreenType.Error -> emit(
                    JobNotificationUiState(
                        screenType = JobNotificationUiState.ScreenType.Error(
                            errorTitle = "Erro ao carregar atividades",
                            errorMessage = ""
                        )
                    )
                )
                is DisciplineUiState.ScreenType.Loaded -> {
                    val result = getJobs(param, it.screenType.listDisciplines)
                    emit(result)
                }
            }
        }

    }

    data class Params(val userUid: String)

    private suspend fun getJobs(
        userUid: String,
        listDisciplines: List<DisciplineModel>,
    ): JobNotificationUiState {
        return try {
            val querySnapshot = getJobQuery(userUid)
                .orderBy("dtDelivery", Query.Direction.ASCENDING)
                .get().await()
            val jobs = querySnapshot.documents.mapNotNull { doc ->
                createJob(doc, listDisciplines)
            }

            JobNotificationUiState(
                screenType = JobNotificationUiState.ScreenType.Loaded(jobs)
            )
        } catch (e: Exception) {
            JobNotificationUiState(
                screenType = JobNotificationUiState.ScreenType.Error(
                    errorTitle = "Erro ao carregar atividades",
                    errorMessage = e.handleFirebaseErrors()
                )
            )
        }
    }

    private fun createJob(doc: DocumentSnapshot, listDisciplines: List<DisciplineModel>): JobModel? {
        return try {
            JobModel(
                id = doc.id,
                disciplineId = doc.getString("disciplineId").orEmpty(),
                nameDiscipline = listDisciplines.firstOrNull { it.id == doc.getString("disciplineId") }?.name.orEmpty(),
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

    private fun getJobQuery(userUid: String): Query {
        var query: Query = db.collection("users")
            .document(userUid)
            .collection("jobs")
            .whereNotIn("status", arrayListOf("S"))

        query = query.whereGreaterThanOrEqualTo("dtDelivery", Timestamp(now().resetTime().time))

        return query
    }
}
