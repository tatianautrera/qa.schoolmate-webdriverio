package com.fsacchi.schoolmate.data.model.file

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.data.model.job.TypeJob
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class FileUserModel(
    val id: String = "",
    var urlFirebase: String = "",
    var disciplineId: String = "",
    var extension: String = "",
    var titleFile: String = "",
    var observation: String = "",
    var nameDiscipline: String = ""
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

    fun getIconDownload(): Int {
        return if (id.isNotEmpty()) {
            R.drawable.ic_download
        } else {
            R.drawable.ic_see
        }
    }
}