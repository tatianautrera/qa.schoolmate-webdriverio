package com.fsacchi.schoolmate.domain.login

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.data.model.login.RegisterUserModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.LoginUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RegisterUserUseCase(
    private val auth: FirebaseAuth
) : UseCase<RegisterUserModel, LoginUiState>() {

    override suspend fun execute(param: RegisterUserModel): Flow<LoginUiState> = flow {
        emit(LoginUiState(LoginUiState.ScreenType.Loading))

        if(param.password != param.confirmPassword) {
            emit(
                LoginUiState(
                    screenType = LoginUiState.ScreenType.Error(
                        errorTitle = "Erro de validação",
                        errorMessage = "As senhas não coincidem"
                    )
                )
            )
        } else {
            val result = createUserWithEmail(param.email, param.password, param.name)
            emit(result)
        }
    }

    private suspend fun createUserWithEmail(email: String, password: String, name: String): LoginUiState {
        return suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }

                        user?.updateProfile(profileUpdates)
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { taskEmail ->
                                if (taskEmail.isSuccessful) {
                                    continuation.resume(
                                        LoginUiState(screenType = LoginUiState.ScreenType.Success)
                                    )
                                } else {
                                    LoginUiState(
                                        screenType = LoginUiState.ScreenType.Error(
                                            errorTitle = "Erro ao criar usuário",
                                            errorMessage = taskEmail.exception?.handleFirebaseErrors()
                                        ),
                                    )
                                }
                            }
                    } else {
                        continuation.resume(
                            LoginUiState(
                                screenType = LoginUiState.ScreenType.Error(
                                    errorTitle = "Erro ao criar usuário",
                                    errorMessage = task.exception?.handleFirebaseErrors()
                                ),
                            )
                        )
                    }
                }
        }
    }


    private fun sendVerificationEmail(user: FirebaseUser?) {

    }
}
