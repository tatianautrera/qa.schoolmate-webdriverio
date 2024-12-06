package com.fsacchi.schoolmate.core.di

import com.fsacchi.schoolmate.domain.login.RegisterUserUseCase
import org.koin.dsl.module

private val loginUseCases = module {
    factory { RegisterUserUseCase(get()) }
}

internal val useCaseModules = listOf(
    loginUseCases,
)
