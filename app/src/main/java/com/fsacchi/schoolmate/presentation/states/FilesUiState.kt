package com.fsacchi.schoolmate.presentation.states

import androidx.compose.runtime.Stable
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.file.FileUserModel
import com.fsacchi.schoolmate.data.model.job.JobModel

@Stable
data class FilesUiState(
    val screenType: ScreenType = ScreenType.Await
) {
    sealed interface ScreenType {
        data object Await: ScreenType
        data class Loaded(
            val listFiles: List<FileUserModel>
        ): ScreenType

        data class Error(
            val errorTitle: String?,
            val errorMessage: String?
        ): ScreenType
    }
}
