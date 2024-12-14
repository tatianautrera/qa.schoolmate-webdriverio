package com.fsacchi.schoolmate.data.model.job

import android.os.Parcelable
import androidx.annotation.StringRes
import com.fsacchi.schoolmate.core.extensions.DateMasks.appFormat
import com.fsacchi.schoolmate.core.extensions.format
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class JobModel(
    val id: String = "",
    var nameDiscipline: String = "",
    var disciplineId: String = "",
    var typeJob: TypeJob? = null,
    var descrTypeJob: String = "",
    var dtJob: Date? = null,
    var observation: String = ""
): Parcelable {
    fun date() = dtJob?.format(appFormat)
}

enum class TypeJob(var message: String) {
    Test("Avaliação"),
    HomeWork("Tarefa"),
    Job("Trabalho")
}