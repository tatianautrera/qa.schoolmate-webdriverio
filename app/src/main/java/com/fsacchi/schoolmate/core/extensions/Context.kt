package com.fsacchi.schoolmate.core.extensions

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

infix fun Context.color(@ColorRes color: Int) = ContextCompat.getColor(this, color)

infix fun Fragment.color(@ColorRes color: Int) = requireContext().color(color)

infix fun Context.drawable(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

fun Context.copyToClipboard(text: CharSequence, @StringRes message: Int) {
    copyToClipboard(text, getString(message))
}

fun Context.copyToClipboard(text: CharSequence, message: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Text", text)
    clipboard.setPrimaryClip(clip)
    toast(message, Toast.LENGTH_LONG)
}

fun Context.getImageBitmap(base64Image: String): Bitmap {
    val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

fun Context.verifyContentClipBoard(): String {
    var textCopied: String = ""
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    if (!clipboard.hasPrimaryClip()) return textCopied
    if (clipboard.primaryClipDescription == null) return textCopied
    if ((!clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)!!) &&
        (!clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)!!)) return textCopied

    val item = clipboard.primaryClip?.getItemAt(0)
    if (item != null && item.text != null) {
        textCopied = item.text.toString()
    }
    return textCopied
}
