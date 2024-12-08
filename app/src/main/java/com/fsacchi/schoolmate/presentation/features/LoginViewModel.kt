package com.fsacchi.schoolmate.presentation.features

import android.content.SharedPreferences
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.core.extensions.fromBase64
import com.fsacchi.schoolmate.data.local.extensions.PreferencesKeys
import com.fsacchi.schoolmate.data.local.extensions.bool
import com.fsacchi.schoolmate.data.local.extensions.string
import com.fsacchi.schoolmate.data.model.login.UserModel
import com.fsacchi.schoolmate.domain.login.ForgotPasswordUseCase
import com.fsacchi.schoolmate.domain.login.LoginUseCase
import com.fsacchi.schoolmate.domain.login.RegisterUserUseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val loginUseCase: LoginUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel(), LifecycleObserver {

    var userModel: UserModel = UserModel()
    var uiState = UiState()

    fun updateLoginSaved() {
        if (sharedPreferences.bool(PreferencesKeys.SAVE_CREDENTIALS.name)) {
            userModel.saveCredentials = true
            userModel.email = sharedPreferences.string(PreferencesKeys.USER_EMAIL.name).orEmpty()
            userModel.password = sharedPreferences.string(PreferencesKeys.USER_PASS.name).orEmpty().fromBase64()
        } else {
            userModel = UserModel()
        }
    }

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
    fun login() {
        viewModelScope.launch {
            loginUseCase(userModel).collect {
                uiState.login.emit(it)
            }
        }
    }

    data class UiState(
        val login: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow()
    )
}