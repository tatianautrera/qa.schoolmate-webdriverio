package com.fsacchi.schoolmate.domain.file

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.file.FileUserModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.domain.discipline.GetDisciplinesUseCase
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.fsacchi.schoolmate.presentation.states.FilesUiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class GetFileUseCase(
    private val db: FirebaseFirestore,
    private val getDisciplinesUseCase: GetDisciplinesUseCase
) : UseCase<GetFileUseCase.Params, FilesUiState>() {

    override suspend fun execute(param: Params): Flow<FilesUiState> = flow {
        emit(FilesUiState(FilesUiState.ScreenType.Await))
        getDisciplinesUseCase.execute(param.userUid).collect {
            when(it.screenType) {
                DisciplineUiState.ScreenType.Await -> {}
                is DisciplineUiState.ScreenType.Error -> emit(
                    FilesUiState(
                        screenType = FilesUiState.ScreenType.Error(
                            errorTitle = "Erro ao carregar arquivos",
                            errorMessage = ""
                        )
                    )
                )
                is DisciplineUiState.ScreenType.Loaded -> {
                    val result = getFiles(param.userUid, param.disciplineSelected, it.screenType.listDisciplines)
                    emit(result)
                }
            }
        }

    }

    data class Params(val userUid: String, val disciplineSelected: DisciplineModel?)

    private suspend fun getFiles(
        userUid: String,
        disciplineSelected: DisciplineModel?,
        listDisciplines: List<DisciplineModel>
    ): FilesUiState {
        return try {
            val querySnapshot = getFilesQuery(userUid, disciplineSelected)
                .orderBy("dtRegister", Query.Direction.DESCENDING)
                .get().await()
            val files = querySnapshot.documents.mapNotNull { doc ->
                try {
                    FileUserModel(
                        id = doc.id,
                        disciplineId = doc.getString("disciplineId").orEmpty(),
                        nameDiscipline = disciplineSelected?.name
                            ?: listDisciplines.firstOrNull { it.id == doc.getString("disciplineId") }?.name.orEmpty(),
                        extension = doc.getString("extension").orEmpty(),
                        urlFirebase = doc.getString("firebaseUrl").orEmpty(),
                        titleFile = doc.getString("title").orEmpty(),
                        observation = doc.getString("observation").orEmpty(),
                    )
                } catch (e: Exception) {
                    null
                }
            }

            FilesUiState(
                screenType = FilesUiState.ScreenType.Loaded(
                    if (disciplineSelected != null) files.filter { file -> file.disciplineId == disciplineSelected.id } else files
                )
            )
        } catch (e: Exception) {
            FilesUiState(
                screenType = FilesUiState.ScreenType.Error(
                    errorTitle = "Erro ao carregar arquivos",
                    errorMessage = e.handleFirebaseErrors()
                )
            )
        }
    }

    private fun getFilesQuery(userUid: String, disciplineSelected: DisciplineModel?): Query {
        var query: Query = db.collection("users")
            .document(userUid)
            .collection("files")

        if (disciplineSelected != null) {
            query = query.whereEqualTo("disciplineId", disciplineSelected.id)
        }
        return query
    }
}
