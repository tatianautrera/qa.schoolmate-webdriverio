package com.fsacchi.schoolmate.data.model.file

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.data.model.job.TypeJob
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class FileModel(
    val id: String = "",
    var nameFile: String = "",
    var extension: String = "",
    var urlFirebase: String = ""
): Parcelable {
    fun getIconExtension(): Int {
        return when(extension.lowercase()) {
            "jpg", "jpeg", "png" -> R.drawable.ic_image
            "doc", "docx" -> R.drawable.ic_doc
            "pdf" -> R.drawable.ic_pdf
            "xls", "xlsx" -> R.drawable.ic_xls
            else -> R.drawable.ic_doc
        }
    }

    fun getLabelFile(): String {
        return when(extension.lowercase()) {
            "jpg", "jpeg", "png" -> "Você adicionou uma imagem"
            "doc", "docx" -> "Você adicionou um documento"
            "pdf" -> "Você adicionou um PDF"
            "xls", "xlsx" -> "Você adicionou uma planilha"
            else -> "Você adicionou um arquivo"
        }
    }

}