package com.fsacchi.schoolmate.presentation.features

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.domain.discipline.DeleteDisciplineUseCase
import com.fsacchi.schoolmate.domain.discipline.GetDisciplinesUseCase
import com.fsacchi.schoolmate.domain.discipline.SaveDisciplineUseCase
import com.fsacchi.schoolmate.domain.discipline.UpdateDisciplineUseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DisciplineViewModel(
    private val saveDisciplineUseCase: SaveDisciplineUseCase,
    private val getDisciplinesUseCase: GetDisciplinesUseCase,
    private val updateDisciplineUseCase: UpdateDisciplineUseCase,
    private val deleteDisciplineUseCase: DeleteDisciplineUseCase
) : ViewModel(), LifecycleObserver {

    val uiState = UiState()

    fun saveDisciplineModel(disciplineModel: DisciplineModel, userUid: String) {
        viewModelScope.launch{
            val params = SaveDisciplineUseCase.Params(userUid, disciplineModel)
            saveDisciplineUseCase(params).collect {
                uiState.saveDiscipline.emit(it)
            }
        }
    }
    fun updateDisciplineModel(disciplineModel: DisciplineModel, userUid: String) {
        viewModelScope.launch{
            val params = UpdateDisciplineUseCase.Params(userUid, disciplineModel)
            updateDisciplineUseCase(params).collect {
                uiState.saveDiscipline.emit(it)
            }
        }
    }
    fun deleteDisciplineModel(disciplineModel: DisciplineModel, userUid: String) {
        viewModelScope.launch{
            val params = DeleteDisciplineUseCase.Params(userUid, disciplineModel)
            deleteDisciplineUseCase(params).collect {
                uiState.deleteDiscipline.emit(it)
            }
        }
    }
    fun getDisciplines(userUid: String) {
        viewModelScope.launch{
            getDisciplinesUseCase(userUid).collect {
                uiState.disciplines.emit(it)
            }
        }
    }

    data class UiState(
        val saveDiscipline: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val deleteDiscipline: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val disciplines: MutableSharedFlow<DisciplineUiState> = MutableSharedFlow()
    )
}