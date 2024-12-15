package com.fsacchi.schoolmate.domain.job

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.core.extensions.resetTime
import com.fsacchi.schoolmate.core.extensions.toDate
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.data.model.job.TypeJob
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.domain.discipline.GetDisciplinesUseCase
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.fsacchi.schoolmate.presentation.states.JobUiState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.sql.Timestamp
import java.util.Calendar
import kotlin.coroutines.resume

class GetJobUseCase(
    private val db: FirebaseFirestore,
    private val getDisciplinesUseCase: GetDisciplinesUseCase
) : UseCase<GetJobUseCase.Params, JobUiState>() {

    override suspend fun execute(param: Params): Flow<JobUiState> = flow {
        emit(JobUiState(JobUiState.ScreenType.Await))
        getDisciplinesUseCase.execute(param.userUid).collect {
            when(it.screenType) {
                DisciplineUiState.ScreenType.Await -> {}
                is DisciplineUiState.ScreenType.Error -> emit(
                    JobUiState(
                        screenType = JobUiState.ScreenType.Error(
                            errorTitle = "Erro ao carregar atividades",
                            errorMessage = ""
                        )
                    )
                )
                is DisciplineUiState.ScreenType.Loaded -> {
                    val result = getJobs(param.userUid, param.disciplineSelected, it.screenType.listDisciplines, param.showOnlyJobsToDelivery)
                    emit(result)
                }
            }
        }

    }

    data class Params(val userUid: String, val disciplineSelected: DisciplineModel?, val showOnlyJobsToDelivery: Boolean)

    private suspend fun getJobs(
        userUid: String,
        disciplineSelected: DisciplineModel?,
        listDisciplines: List<DisciplineModel>,
        showOnlyJobsToDelivery: Boolean
    ): JobUiState {
        return try {
            val querySnapshot = getJobQuery(userUid, disciplineSelected, showOnlyJobsToDelivery)
                .orderBy("dtDelivery", Query.Direction.ASCENDING)
                .get().await()
            val jobs = querySnapshot.documents.mapNotNull { doc ->
                try {
                    JobModel(
                        id = doc.id,
                        disciplineId = doc.getString("disciplineId").orEmpty(),
                        nameDiscipline = disciplineSelected?.name
                            ?: listDisciplines.firstOrNull { it.id == doc.getString("disciplineId") }?.name.orEmpty(),
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

            JobUiState(
                screenType = JobUiState.ScreenType.Loaded(
                    if (disciplineSelected != null) jobs.filter { job -> job.disciplineId == disciplineSelected.id } else jobs
                )
            )
        } catch (e: Exception) {
            JobUiState(
                screenType = JobUiState.ScreenType.Error(
                    errorTitle = "Erro ao carregar atividades",
                    errorMessage = e.handleFirebaseErrors()
                )
            )
        }
    }

    private fun getJobQuery(userUid: String, disciplineSelected: DisciplineModel?, showOnlyJobsToDelivery: Boolean): Query {
        var query: Query = db.collection("users")
            .document(userUid)
            .collection("jobs") // Inicializando como Query

        if (disciplineSelected != null) {
            query = query.whereEqualTo("disciplineId", disciplineSelected.id)
        }
        if (showOnlyJobsToDelivery) {
            query = query.whereGreaterThanOrEqualTo("dtDelivery", Timestamp(now().resetTime().time))
        }
        return query
    }
}
