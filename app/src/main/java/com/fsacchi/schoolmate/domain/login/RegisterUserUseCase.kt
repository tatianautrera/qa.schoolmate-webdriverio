package com.fsacchi.schoolmate.domain.login

import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.data.model.login.UserModel
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RegisterUserUseCase(
    private val auth: FirebaseAuth
) : UseCase<UserModel, DefaultUiState>() {

    override suspend fun execute(param: UserModel): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))

        if(param.password != param.confirmPassword) {
            emit(
                DefaultUiState(
                    screenType = DefaultUiState.ScreenType.Error(
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

    private suspend fun createUserWithEmail(email: String, password: String, name: String): DefaultUiState {
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
                                        DefaultUiState(screenType = DefaultUiState.ScreenType.Success)
                                    )
                                } else {
                                    DefaultUiState(
                                        screenType = DefaultUiState.ScreenType.Error(
                                            errorTitle = "Erro ao criar usuário",
                                            errorMessage = taskEmail.exception?.handleFirebaseErrors()
                                        ),
                                    )
                                }
                            }
                    } else {
                        continuation.resume(
                            DefaultUiState(
                                screenType = DefaultUiState.ScreenType.Error(
                                    errorTitle = "Erro ao criar usuário",
                                    errorMessage = task.exception?.handleFirebaseErrors()
                                ),
                            )
                        )
                    }
                }
        }
    }
}
