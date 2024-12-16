package com.fsacchi.schoolmate.presentation.features

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.file.FileUserModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.domain.discipline.DeleteDisciplineUseCase
import com.fsacchi.schoolmate.domain.discipline.GetDisciplinesUseCase
import com.fsacchi.schoolmate.domain.discipline.SaveDisciplineUseCase
import com.fsacchi.schoolmate.domain.discipline.UpdateDisciplineUseCase
import com.fsacchi.schoolmate.domain.file.DeleteFileUseCase
import com.fsacchi.schoolmate.domain.file.GetFileUseCase
import com.fsacchi.schoolmate.domain.file.SaveFileUseCase
import com.fsacchi.schoolmate.domain.file.UpdateFileUseCase
import com.fsacchi.schoolmate.domain.job.DeleteJobUseCase
import com.fsacchi.schoolmate.domain.job.GetJobUseCase
import com.fsacchi.schoolmate.domain.job.SaveJobUseCase
import com.fsacchi.schoolmate.domain.job.UpdateJobUseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.fsacchi.schoolmate.presentation.states.FilesUiState
import com.fsacchi.schoolmate.presentation.states.JobUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class FileViewModel(
    private val saveFileUseCase: SaveFileUseCase,
    private val getFileUseCase: GetFileUseCase,
    private val updateFileUseCase: UpdateFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase
) : ViewModel(), LifecycleObserver {

    val uiState = UiState()

    fun saveFileModel(fileUserModel: FileUserModel, userUid: String) {
        viewModelScope.launch{
            val params = SaveFileUseCase.Params(userUid, fileUserModel)
            saveFileUseCase(params).collect {
                uiState.saveFile.emit(it)
            }
        }
    }

    fun getFiles(userUid: String, disciplineSelected: DisciplineModel?) {
        viewModelScope.launch{
            val params = GetFileUseCase.Params(userUid, disciplineSelected)
            getFileUseCase(params).collect {
                uiState.files.emit(it)
            }
        }
    }

    fun updateFileModel(fileUserModel: FileUserModel, userUid: String) {
        viewModelScope.launch{
            val params = UpdateFileUseCase.Params(userUid, fileUserModel)
            updateFileUseCase(params).collect {
                uiState.saveFile.emit(it)
            }
        }
    }
    fun deleteFileModel(fileUserModel: FileUserModel, userUid: String) {
        viewModelScope.launch{
            val params = DeleteFileUseCase.Params(userUid, fileUserModel)
            deleteFileUseCase(params).collect {
                uiState.deleteFile.emit(it)
            }
        }
    }

    data class UiState(
        val saveFile: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val deleteFile: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val files: MutableSharedFlow<FilesUiState?> = MutableSharedFlow()
    )
}