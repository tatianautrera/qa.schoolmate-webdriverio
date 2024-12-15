package com.fsacchi.schoolmate.domain.job

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class UpdateJobUseCase(
    private val db: FirebaseFirestore
) : UseCase<UpdateJobUseCase.Params, DefaultUiState>() {

    override suspend fun execute(param: Params): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))
        val result = updateJob(param.userUid, param.jobModel)
        emit(result)
    }
    data class Params(val userUid: String, val jobModel: JobModel)

    private suspend fun updateJob(userUid: String, jobModel: JobModel): DefaultUiState {
        return suspendCancellableCoroutine { continuation ->
            val job = mapOf(
                "disciplineId" to jobModel.disciplineId,
                "dtDelivery" to jobModel.dtJob,
                "jobType" to jobModel.typeJob?.name,
                "observation" to jobModel.observation,
                "status" to jobModel.status
            )

            db.collection("users")
                .document(userUid)
                .collection("jobs")
                .document(jobModel.id)
                .set(job)
                .addOnSuccessListener { _ ->
                    continuation.resume(
                        DefaultUiState(
                            screenType = DefaultUiState.ScreenType.Success
                        )
                    )
                }
                .addOnFailureListener { e ->
                    continuation.resume(
                        DefaultUiState(
                            screenType = DefaultUiState.ScreenType.Error(
                                errorTitle = "Erro ao atualizar atividade",
                                errorMessage = e.handleFirebaseErrors()
                            )
                        )
                    )
                }
        }
    }
}
