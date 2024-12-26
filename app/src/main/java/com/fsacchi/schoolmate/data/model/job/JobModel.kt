package com.fsacchi.schoolmate.data.model.job

import android.os.Parcelable
import androidx.annotation.StringRes
import com.fsacchi.schoolmate.core.extensions.DateMasks.appFormat
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.daysBetweenNow
import com.fsacchi.schoolmate.core.extensions.format
import com.fsacchi.schoolmate.core.extensions.isToday
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
    var observation: String = "",
    var status: String = ""
): Parcelable {
    fun date() = dtJob?.format(appFormat)
    fun dateToDelivery() = "Para ${date()}"
    fun titleJob() = when(typeJob) {
        TypeJob.Test -> "Avaliação de ${nameDiscipline.capitalizeFirstLetter()}"
        TypeJob.HomeWork -> "Tarefa de ${nameDiscipline.capitalizeFirstLetter()}"
        TypeJob.Job -> "Trabalho de ${nameDiscipline.capitalizeFirstLetter()}"
        null -> ""
    }

    fun messageNotificationJob(): String {
        val timeMessage = when (dtJob?.daysBetweenNow()) {
            0 -> "hoje!"
            1 -> "amanhã!"
            else -> "daqui a ${dtJob?.daysBetweenNow()} dias."
        }

        return when(typeJob) {
            TypeJob.Test -> "Avaliação de ${nameDiscipline.capitalizeFirstLetter()} $timeMessage Já estudou?"
            TypeJob.HomeWork -> "Tarefa de ${nameDiscipline.capitalizeFirstLetter()} Não deixe pra última hora."
            TypeJob.Job -> "Trabalho de ${nameDiscipline.capitalizeFirstLetter()} Faça um belo trabalho!"
            null -> ""
        }
    }
    fun isFinish() = status == "S"
}

enum class TypeJob(var message: String) {
    Test("Avaliação"),
    HomeWork("Tarefa"),
    Job("Trabalho")
}