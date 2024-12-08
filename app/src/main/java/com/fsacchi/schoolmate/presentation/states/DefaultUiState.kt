package com.fsacchi.schoolmate.presentation.states

import androidx.compose.runtime.Stable

@Stable
data class DefaultUiState(
    val screenType: ScreenType = ScreenType.Loading
) {
    sealed interface ScreenType {
        data object Loading: ScreenType
        data class Error(
            val errorTitle: String?,
            val errorMessage: String?
        ): ScreenType
        data object Success: ScreenType
    }
}
