package com.fsacchi.schoolmate.domain.discipline

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class UpdateDisciplineUseCase(
    private val db: FirebaseFirestore
) : UseCase<UpdateDisciplineUseCase.Params, DefaultUiState>() {

    override suspend fun execute(param: Params): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))
        val result = updateDiscipline(param.userUid, param.disciplineModel)
        emit(result)
    }
    data class Params(val userUid: String, val disciplineModel: DisciplineModel)

    private suspend fun updateDiscipline(userUid: String, disciplineModel: DisciplineModel): DefaultUiState {
        return suspendCancellableCoroutine { continuation ->
            val discipline = mapOf(
                "nameDiscipline" to disciplineModel.name,
                "teacherDiscipline" to disciplineModel.teacher,
                "emoji" to disciplineModel.emoji
            )

            db.collection("users")
                .document(userUid)
                .collection("disciplines")
                .document(disciplineModel.id)
                .set(discipline)
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
                                errorTitle = "Erro ao atualizar disciplina",
                                errorMessage = e.handleFirebaseErrors()
                            )
                        )
                    )
                }
        }
    }
}
