package com.fsacchi.schoolmate.domain.job

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.core.extensions.unaccent
import com.fsacchi.schoolmate.core.extensions.unmask
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class SaveJobUseCase(
    private val db: FirebaseFirestore,
) : UseCase<SaveJobUseCase.Params, DefaultUiState>() {

    override suspend fun execute(param: Params): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))
        val result = saveJob(param.userUid, param.jobModel)
        emit(result)
    }

    data class Params(val userUid: String, val jobModel: JobModel)

    private suspend fun saveJob(userUid: String, jobModel: JobModel): DefaultUiState {
        return suspendCancellableCoroutine { continuation ->
            val job = mapOf(
                "disciplineId" to jobModel.disciplineId,
                "dtDelivery" to jobModel.dtJob,
                "jobType" to jobModel.typeJob?.name,
                "observation" to jobModel.observation,
                "status" to ""
            )

            db.collection("users")
                .document(userUid)
                .collection("jobs")
                .add(job)
                .addOnSuccessListener {
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
                                errorTitle = "Erro ao gravar atividade",
                                errorMessage = e.handleFirebaseErrors()
                            )
                        )
                    )
                }

        }
    }
}

