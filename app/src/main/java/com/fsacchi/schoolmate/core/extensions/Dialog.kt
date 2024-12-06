package com.fsacchi.schoolmate.core.extensions

import android.R.attr
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.widget.Button
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.databinding.ProgressBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//region Create
fun Context.createDialog(block: MaterialAlertDialogBuilder.() -> Unit = {}): AlertDialog {
    val builder = MaterialAlertDialogBuilder(this)
    builder.setPositiveMessage(android.R.string.ok)
    block(builder)
    return builder.create().apply {
        setOnDismissListener {
            window?.decorView?.hideSoftKeyboard()
        }

        setOnShowListener {
            val color = getColor(R.color.colorPrimary)
            getButton(AlertDialog.BUTTON_POSITIVE)?.setupTheme(color)
            getButton(AlertDialog.BUTTON_NEGATIVE)?.setupTheme(color)
            getButton(AlertDialog.BUTTON_NEUTRAL)?.setupTheme(color)
        }
    }
}

private fun Button.setupTheme(color: Int) {
    val states = arrayOf(intArrayOf(attr.state_enabled, attr.state_pressed))
    val colors = intArrayOf(ColorUtils.setAlphaComponent(color, 60))

    backgroundTintList = ColorStateList(states, colors)
    setTextColor(color)
}

fun Fragment.createDialog(block: MaterialAlertDialogBuilder.() -> Unit = {}): AlertDialog {
    return requireContext().createDialog(block)
}

fun Fragment.createProgressDialog(): AlertDialog {
    return requireContext().createProgressDialog()
}

fun Context.createProgressDialog(): AlertDialog {
    return createDialog {
        val inflater = LayoutInflater.from(this@createProgressDialog)
        val inflate = DataBindingUtil.inflate<ProgressBinding>(inflater, R.layout.progress, null, false)

        setView(inflate.root)

        setPositiveButton(null, null)
        setCancelable(false)
    }.apply {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
//endregion

//region Show
fun Fragment.showDialog(block: MaterialAlertDialogBuilder.() -> Unit = {}) {
    requireContext().createDialog(block).show()
}

fun AppCompatActivity.showDialog(block: MaterialAlertDialogBuilder.() -> Unit = {}) {
    createDialog(block).show()
}

fun Context.showDialog(block: MaterialAlertDialogBuilder.() -> Unit = {}) {
    createDialog(block).show()
}
//endregion

//region Listeners
typealias Dialog = AlertDialog.Builder

fun Dialog.setPositiveMessage(message: String, listener: () -> Unit = {}) {
    setPositiveButton(message) { _, _ -> listener() }
}

fun Dialog.setPositiveMessage(@StringRes message: Int, listener: () -> Unit = {}) {
    setPositiveMessage(context.getString(message), listener)
}

fun Dialog.setPositiveListener(listener: () -> Unit) {
    setPositiveButton(android.R.string.ok) { _, _ -> listener() }
}

fun Dialog.setNegativeMessage(message: String, listener: () -> Unit = {}) {
    setNegativeButton(message) { _, _ -> listener() }
}

fun Dialog.setNegativeMessage(@StringRes message: Int, listener: () -> Unit = {}) {
    setNegativeMessage(context.getString(message), listener)
}
//endregion
