package com.fsacchi.schoolmate.presentation.features

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.repository.UserRepository
import com.fsacchi.schoolmate.domain.discipline.GetDisciplinesUseCase
import com.fsacchi.schoolmate.domain.discipline.SaveDisciplineUseCase
import com.fsacchi.schoolmate.domain.home.GetUserUseCase
import com.fsacchi.schoolmate.domain.home.LogoffUseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DisciplineViewModel(
    private val saveDisciplineUseCase: SaveDisciplineUseCase,
    private val getDisciplinesUseCase: GetDisciplinesUseCase
) : ViewModel(), LifecycleObserver {

    val uiState = UiState()

    fun saveDisciplineModel(disciplineModel: DisciplineModel, userUid: String) {
        viewModelScope.launch{
            val params = SaveDisciplineUseCase.Params(userUid, disciplineModel)
            saveDisciplineUseCase(params).collect {
                uiState.newDiscipline.emit(it)
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
        val newDiscipline: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val disciplines: MutableSharedFlow<DisciplineUiState> = MutableSharedFlow()
    )
}