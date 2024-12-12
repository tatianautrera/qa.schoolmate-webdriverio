package com.fsacchi.schoolmate.core.di

import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.features.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val loginModules = module {
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
}

private val disciplineModules = module {
    viewModel { DisciplineViewModel(get(), get(), get(), get()) }
}

internal val viewModelModules = listOf(
    loginModules,
    disciplineModules
)
