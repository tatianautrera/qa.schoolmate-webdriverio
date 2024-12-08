package com.fsacchi.schoolmate.domain.home

import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.repository.UserRepository
import com.fsacchi.schoolmate.domain.UseCase
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserUseCase(
    private val repository: UserRepository
) : UseCase.NoParam<UserUiState>() {

    override suspend fun execute(): Flow<UserUiState> = flow {
        emit(UserUiState(UserUiState.ScreenType.Await))
        repository.getUser().collect {
            emit(UserUiState(UserUiState.ScreenType.Loaded(it)))
        }
    }

}
