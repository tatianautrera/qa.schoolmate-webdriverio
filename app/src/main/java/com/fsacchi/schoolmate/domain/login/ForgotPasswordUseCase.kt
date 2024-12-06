package com.fsacchi.schoolmate.domain.login

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.core.extensions.isEmailValid
import com.fsacchi.schoolmate.data.model.login.UserModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.LoginUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ForgotPasswordUseCase(
    private val auth: FirebaseAuth
) : UseCase<UserModel, LoginUiState>() {

    override suspend fun execute(param: UserModel): Flow<LoginUiState> = flow {
        emit(LoginUiState(LoginUiState.ScreenType.Loading))
        val result = sendEmailForgotPassword(param.email)
        emit(result)
    }

    private suspend fun sendEmailForgotPassword(email: String): LoginUiState {
        return suspendCancellableCoroutine { continuation ->
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(
                            LoginUiState(screenType = LoginUiState.ScreenType.Success)
                        )
                    } else {
                        LoginUiState(
                            screenType = LoginUiState.ScreenType.Error(
                                errorTitle = "Erro ao enviar email",
                                errorMessage = task.exception?.handleFirebaseErrors()
                            ),
                        )
                    }
                }
        }
    }

}
