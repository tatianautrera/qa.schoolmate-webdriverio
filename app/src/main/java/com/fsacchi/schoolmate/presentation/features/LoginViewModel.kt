package com.fsacchi.schoolmate.presentation.features

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.data.model.login.RegisterUserModel
import com.fsacchi.schoolmate.domain.login.RegisterUserUseCase
import com.fsacchi.schoolmate.presentation.states.LoginUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel(), LifecycleObserver {

    val registerUserModel = RegisterUserModel()
    var uiState = UiState()

    fun registerUser() {
        viewModelScope.launch {
            registerUserUseCase(registerUserModel).collect {
                uiState.login.emit(it)
            }
        }
    }

    data class UiState(
        val login: MutableSharedFlow<LoginUiState?> = MutableSharedFlow()
    )
}