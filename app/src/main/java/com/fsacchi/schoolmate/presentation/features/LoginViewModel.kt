package com.fsacchi.schoolmate.presentation.features

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.data.model.login.UserModel
import com.fsacchi.schoolmate.domain.login.ForgotPasswordUseCase
import com.fsacchi.schoolmate.domain.login.RegisterUserUseCase
import com.fsacchi.schoolmate.presentation.states.LoginUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel(), LifecycleObserver {

    var userModel: UserModel = UserModel()

    var uiState = UiState()

    fun registerUser() {
        viewModelScope.launch {
            registerUserUseCase(userModel).collect {
                uiState.login.emit(it)
            }
        }
    }

    fun forgotPassword() {
        viewModelScope.launch {
            forgotPasswordUseCase(userModel).collect {
                uiState.login.emit(it)
            }
        }
    }

    data class UiState(
        val login: MutableSharedFlow<LoginUiState?> = MutableSharedFlow()
    )
}