package com.fsacchi.schoolmate.core.features.home

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fsacchi.schoolmate.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class OptionItem(@StringRes val desc: Int, @DrawableRes val icon: Int) : Parcelable

val disciplineMenuItems = listOf(
    OptionItem(R.string.see_details, R.drawable.ic_enter),
    OptionItem(R.string.edit, R.drawable.ic_edit),
    OptionItem(R.string.delete, R.drawable.ic_delete)
)

val jobMenuItems = listOf(
    OptionItem(R.string.edit, R.drawable.ic_edit),
    OptionItem(R.string.delete, R.drawable.ic_delete)
)