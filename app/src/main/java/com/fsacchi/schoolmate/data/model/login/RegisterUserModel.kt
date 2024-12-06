package com.fsacchi.schoolmate.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterUserModel(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = ""
): Parcelable