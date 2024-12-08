package com.fsacchi.schoolmate.domain.home

import com.fsacchi.schoolmate.data.repository.UserRepository
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LogoffUseCase(
    private val userRepository: UserRepository
) : UseCase.NoParam<DefaultUiState>() {

    override suspend fun execute(): Flow<DefaultUiState> = flow {
        emit(DefaultUiState(DefaultUiState.ScreenType.Loading))
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.clear()
        }
        emit(DefaultUiState(DefaultUiState.ScreenType.Success))
    }
}

