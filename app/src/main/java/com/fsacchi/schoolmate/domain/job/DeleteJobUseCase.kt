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

class DeleteJobUseCase(
    private val db: FirebaseFirestore
) : UseCase<DeleteJobUseCase.Params, DefaultUiState>() {

    override suspend fun execute(param: Params): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))
        val result = deleteJob(param.userUid, param.jobModel)
        emit(result)
    }
    data class Params(val userUid: String, val jobModel: JobModel)

    private suspend fun deleteJob(userUid: String, jobModel: JobModel): DefaultUiState {
        return suspendCancellableCoroutine { continuation ->
            db.collection("users")
                .document(userUid)
                .collection("jobs")
                .document(jobModel.id)
                .delete()
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
                                errorTitle = "Erro ao excluir atividade",
                                errorMessage = e.handleFirebaseErrors()
                            )
                        )
                    )
                }
        }
    }
}
