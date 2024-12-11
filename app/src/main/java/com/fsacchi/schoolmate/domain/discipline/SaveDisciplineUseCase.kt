package com.fsacchi.schoolmate.domain.discipline

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.core.extensions.unaccent
import com.fsacchi.schoolmate.core.extensions.unmask
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class SaveDisciplineUseCase(
    private val db: FirebaseFirestore,
) : UseCase<SaveDisciplineUseCase.Params, DefaultUiState>() {

    override suspend fun execute(param: Params): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))
        val result = saveDiscipline(param.userUid, param.disciplineModel)
        emit(result)
    }

    data class Params(val userUid: String, val disciplineModel: DisciplineModel)

    private suspend fun saveDiscipline(userUid: String, disciplineModel: DisciplineModel): DefaultUiState {
        return suspendCancellableCoroutine { continuation ->

            db.collection("users")
                .document(userUid)
                .collection("disciplines")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val existingDiscipline = querySnapshot.documents
                        .mapNotNull { doc ->
                            val nameDiscipline = doc.getString("nameDiscipline")?.unaccent()
                            if (nameDiscipline?.lowercase() == disciplineModel.name.trim().lowercase().unaccent()) {
                                return@mapNotNull doc
                            }
                            null
                        }

                    if (existingDiscipline.isNotEmpty()) {
                        continuation.resume(
                            DefaultUiState(
                                screenType = DefaultUiState.ScreenType.Error(
                                    errorTitle = "Disciplina já existente",
                                    errorMessage = "Já existe uma disciplina com esse nome"
                                )
                            )
                        )
                    } else {
                        val discipline = mapOf(
                            "nameDiscipline" to disciplineModel.name,
                            "teacherDiscipline" to disciplineModel.teacher,
                            "emoji" to disciplineModel.emoji
                        )

                        db.collection("users")
                            .document(userUid)
                            .collection("disciplines")
                            .add(discipline)
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
                                            errorTitle = "Erro ao gravar disciplina",
                                            errorMessage = e.handleFirebaseErrors()
                                        )
                                    )
                                )
                            }
                    }
                }.addOnFailureListener {
                    continuation.resume(
                        DefaultUiState(
                            screenType = DefaultUiState.ScreenType.Error(
                                errorTitle = "Erro ao gravar disciplina",
                                errorMessage = ""
                            )
                        )
                    )
                }
        }
    }
}

