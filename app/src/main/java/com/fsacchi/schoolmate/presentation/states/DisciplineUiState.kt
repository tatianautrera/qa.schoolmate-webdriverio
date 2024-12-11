package com.fsacchi.schoolmate.presentation.states

import androidx.compose.runtime.Stable
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel

@Stable
data class DisciplineUiState(
    val screenType: ScreenType = ScreenType.Await
) {
    sealed interface ScreenType {
        data object Await: ScreenType
        data class Loaded(
            val listDisciplines: List<DisciplineModel>
        ): ScreenType

        data class Error(
            val errorTitle: String?,
            val errorMessage: String?
        ): ScreenType
    }
}
