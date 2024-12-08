package com.fsacchi.schoolmate.presentation.states

import androidx.compose.runtime.Stable
import com.fsacchi.schoolmate.data.local.entity.UserEntity

@Stable
data class UserUiState(
    val screenType: ScreenType = ScreenType.Await
) {
    sealed interface ScreenType {
        data object Await: ScreenType
        data class Loaded(
            val userEntity: UserEntity?
        ): ScreenType
    }
}
