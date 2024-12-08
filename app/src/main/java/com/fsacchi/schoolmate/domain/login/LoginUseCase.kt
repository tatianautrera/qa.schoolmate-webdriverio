package com.fsacchi.schoolmate.domain.login

import android.content.SharedPreferences
import com.fsacchi.schoolmate.core.extensions.handleFirebaseErrors
import com.fsacchi.schoolmate.core.extensions.toBase64
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.local.extensions.PreferencesKeys
import com.fsacchi.schoolmate.data.local.extensions.put
import com.fsacchi.schoolmate.data.model.login.UserModel
import com.fsacchi.schoolmate.data.repository.UserRepository
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LoginUseCase(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : UseCase<UserModel, DefaultUiState>() {

    override suspend fun execute(param: UserModel): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))
        val result = loginWithEmailAndPassword(param.email, param.password, param.saveCredentials)
        emit(result)
    }

    private suspend fun loginWithEmailAndPassword(email: String, password: String, shouldSaveCredentials: Boolean): DefaultUiState {
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            manageSaveCredentials(shouldSaveCredentials, email, password)

                            CoroutineScope(Dispatchers.IO).launch {
                                userRepository.clear()
                                userRepository.save(
                                    UserEntity(
                                        uid = user.uid,
                                        email = user.email.orEmpty(),
                                        name = user.displayName.orEmpty(),
                                        password = password.toBase64()
                                    )
                                )
                            }

                            continuation.resume(
                                DefaultUiState(
                                    screenType = DefaultUiState.ScreenType.Success
                                )
                            )
                        } else {
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { _ ->
                                    continuation.resume(
                                        DefaultUiState(
                                            screenType = DefaultUiState.ScreenType.Error(
                                                errorTitle = "Email não verificado",
                                                errorMessage = "Por favor, verifique seu email antes de fazer login."
                                            )
                                        )
                                    )
                                }
                        }
                    } else {
                        continuation.resume(
                            DefaultUiState(
                                screenType = DefaultUiState.ScreenType.Error(
                                    errorTitle = "Erro no login",
                                    errorMessage = task.exception?.handleFirebaseErrors()
                                )
                            )
                        )
                    }
                }
        }
    }

    private fun manageSaveCredentials(shouldSaveCredentials: Boolean, email: String, password: String) {
        sharedPreferences.apply {
            if (shouldSaveCredentials) {
                put(PreferencesKeys.SAVE_CREDENTIALS.name, true)
                put(PreferencesKeys.USER_EMAIL.name, email)
                put(PreferencesKeys.USER_PASS.name, password.toBase64())
            } else {
                put(PreferencesKeys.SAVE_CREDENTIALS.name, false)
                put(PreferencesKeys.USER_EMAIL.name, "")
                put(PreferencesKeys.USER_PASS.name, "")
            }
        }

    }
}

