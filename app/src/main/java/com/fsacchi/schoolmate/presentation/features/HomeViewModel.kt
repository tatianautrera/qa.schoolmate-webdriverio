package com.fsacchi.schoolmate.presentation.features

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.repository.UserRepository
import com.fsacchi.schoolmate.domain.home.GetUserUseCase
import com.fsacchi.schoolmate.domain.home.LogoffUseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val logoffUseCase: LogoffUseCase,
) : ViewModel(), LifecycleObserver {

    private var user: UserEntity? = null
    val uiState = UiState()

    fun logoff() {
        viewModelScope.launch{
            logoffUseCase().collect {
                uiState.logoff.emit(it)
            }
        }
    }
    fun getUser() {
        viewModelScope.launch {
            getUserUseCase().collect {
                uiState.user.emit(it)
            }
        }
    }

    data class UiState(
        val logoff: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val user: MutableSharedFlow<UserUiState?> = MutableSharedFlow()
    )
}