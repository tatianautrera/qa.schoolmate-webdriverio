package com.fsacchi.schoolmate.data.model.discipline

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DisciplineModel(
    var name: String = "",
    var teacher: String = "",
    var emoji: String = "128525" // emoji carinha feliz
): Parcelable