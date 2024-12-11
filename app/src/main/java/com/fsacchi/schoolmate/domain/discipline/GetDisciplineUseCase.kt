package com.fsacchi.schoolmate.domain.discipline

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GetDisciplinesUseCase(
    private val db: FirebaseFirestore
) : UseCase<String, DisciplineUiState>() {

    override suspend fun execute(param: String): Flow<DisciplineUiState> = flow {
        emit(DisciplineUiState(DisciplineUiState.ScreenType.Await))
        val result = getDisciplines(param)
        emit(result)
    }

    private suspend fun getDisciplines(userUid: String): DisciplineUiState {
        return suspendCancellableCoroutine { continuation ->
            db.collection("users")
                .document(userUid)
                .collection("disciplines")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val disciplines = querySnapshot.documents.mapNotNull { doc ->
                        try {
                            DisciplineModel(
                                name = doc.getString("nameDiscipline").orEmpty(),
                                teacher = doc.getString("teacherDiscipline").orEmpty(),
                                emoji = doc.getString("emoji").orEmpty()
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    continuation.resume(
                        DisciplineUiState(
                            screenType = DisciplineUiState.ScreenType.Loaded(
                                disciplines
                            )
                        )
                    )
                }
                .addOnFailureListener { e ->
                    continuation.resume(
                        DisciplineUiState(
                            screenType = DisciplineUiState.ScreenType.Error(
                                errorTitle = "Erro ao carregar disciplinas",
                                errorMessage = e.handleFirebaseErrors()
                            )
                        )
                    )
                }
        }
    }
}
