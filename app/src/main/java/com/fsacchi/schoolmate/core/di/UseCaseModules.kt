package com.fsacchi.schoolmate.core.di

import com.fsacchi.schoolmate.domain.home.GetUserUseCase
import com.fsacchi.schoolmate.domain.home.LogoffUseCase
import com.fsacchi.schoolmate.domain.login.ForgotPasswordUseCase
import com.fsacchi.schoolmate.domain.login.LoginUseCase
import com.fsacchi.schoolmate.domain.login.RegisterUserUseCase
import org.koin.dsl.module

private val loginUseCases = module {
    factory { RegisterUserUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) }
    factory { LoginUseCase(get(), get(), get()) }
    factory { LogoffUseCase(get()) }
    factory { GetUserUseCase(get()) }
}

internal val useCaseModules = listOf(
    loginUseCases,
)
