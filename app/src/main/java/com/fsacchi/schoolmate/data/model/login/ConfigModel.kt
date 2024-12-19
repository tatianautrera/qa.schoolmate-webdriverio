package com.fsacchi.schoolmate.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfigModel(
    var allowNotification: Boolean,
    var daysUntilHomeWork: String = "3",
    var daysUntilTest: String = "3",
    var daysUntilJob: String = "3",
): Parcelable