package com.fsacchi.schoolmate.validator.extension

import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.databinding.adapters.ListenerUtil
import com.fsacchi.schoolmate.R
import com.google.android.material.textfield.TextInputLayout

internal typealias Input = TextInputLayout

internal var TextInputLayout.text: String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }

fun Input.removeError() = putError(null)

infix fun Input.putError(errorMessage: String?) {
    isErrorEnabled = !TextUtils.isEmpty(errorMessage)
    error = errorMessage
}

fun Input.disableErrorOnChanged() {
    if (ListenerUtil.getListener<TextWatcher>(this, R.id.text_watcher_clear_error) != null) return
    editText?.doOnTextChanged { _, _, _, _ ->
        putError(null)
    }
    ListenerUtil.trackListener(this, this, R.id.text_watcher_clear_error)
}

fun View.message(errorMessage: String?, @StringRes defaultMessage: Int): String {
    return errorMessage ?: context.getString(defaultMessage)
}

fun View.message(@StringRes errorMessage: Int?, @StringRes defaultMessage: Int): String {
    return errorMessage?.let { context.getString(it) } ?: context.getString(defaultMessage)
}

fun View.message(@StringRes message: Int): String {
    return context.getString(message)
}

fun View.message(errorMessage: String?, @StringRes defaultMessage: Int, value: Int): String {
    return errorMessage ?: context.getString(defaultMessage, value)
}
