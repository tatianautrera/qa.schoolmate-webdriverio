package com.fsacchi.schoolmate.data.model.file

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.data.model.job.TypeJob
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class FileUserModel(
    val id: String = "",
    var urlFirebase: String = "",
    var disciplineId: String = "",
    var titleFile: String = "",
    var observation: String = "",
    var nameDiscipline: String = ""
): Parcelable