package com.fsacchi.schoolmate.presentation.states

import android.graphics.Bitmap
import androidx.compose.runtime.Stable

@Stable
data class LoginUiState(
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
