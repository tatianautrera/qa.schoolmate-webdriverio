package com.fsacchi.schoolmate.core.di

import com.fsacchi.schoolmate.presentation.features.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val loginModules = module {
    viewModel { LoginViewModel(get()) }
}

internal val viewModelModules = listOf(
    loginModules
)
