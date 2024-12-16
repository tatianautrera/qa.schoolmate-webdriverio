package com.fsacchi.schoolmate.domain.file

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.core.extensions.resetTime
import com.fsacchi.schoolmate.core.extensions.unaccent
import com.fsacchi.schoolmate.core.extensions.unmask
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.file.FileUserModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class SaveFileUseCase(
    private val db: FirebaseFirestore,
) : UseCase<SaveFileUseCase.Params, DefaultUiState>() {

    override suspend fun execute(param: Params): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))
        val result = saveFile(param.userUid, param.fileUserModel)
        emit(result)
    }

    data class Params(val userUid: String, val fileUserModel: FileUserModel)

    private suspend fun saveFile(userUid: String, fileUserModel: FileUserModel): DefaultUiState {
        return suspendCancellableCoroutine { continuation ->

            val file = mapOf(
                "disciplineId" to fileUserModel.disciplineId,
                "dtRegister" to now().resetTime(),
                "extension" to fileUserModel.extension,
                "firebaseUrl" to fileUserModel.urlFirebase,
                "observation" to fileUserModel.observation,
                "title" to fileUserModel.titleFile,
            )

            db.collection("users")
                .document(userUid)
                .collection("files")
                .add(file)
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
                                errorTitle = "Erro ao gravar arquivo",
                                errorMessage = e.handleFirebaseErrors()
                            )
                        )
                    )
                }

        }
    }
}

