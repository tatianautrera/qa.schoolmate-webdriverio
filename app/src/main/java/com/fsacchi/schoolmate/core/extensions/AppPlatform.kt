package com.fsacchi.schoolmate.core.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Base64
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.AlertMessageDialog
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.nio.charset.StandardCharsets

val FragmentManager.currentNavigationFragment: Fragment?
    get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

inline fun <reified T : BaseActivity<*>> Context.startActivity(
    finishPrevious: Boolean = false,
    bundle: Bundle? = null
) {
    startActivity(Intent(this, T::class.java).apply {
        if (finishPrevious) addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        bundle?.let { putExtras(it) }
    })
}
fun AppCompatActivity.showMessage(builder: AlertMessageDialog.Builder.() -> Unit) {
    AlertMessageDialog.Builder(this).apply { builder(this) }.build().show(supportFragmentManager)
}
fun Fragment.showMessage(builder: AlertMessageDialog.Builder.() -> Unit) {
    (requireActivity() as AppCompatActivity).showMessage(builder)
}
inline fun <reified T : BaseActivity<*>> Fragment.startActivity(finishPrevious: Boolean = false) {
    requireContext().startActivity<T>(finishPrevious)
}

inline fun <reified T : Parcelable> BottomSheetDialogFragment.getParcelable(extra: String) = lazy {
    arguments?.getParcelable<T>(extra)!!
}

fun BottomSheetDialogFragment.getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels

fun BottomSheetDialogFragment.string(key: String) = lazy {
    arguments?.getString(key) ?: ""
}

inline fun <reified T : BaseActivity<*>> Fragment.startActivity(
    finishPrevious: Boolean = false,
    bundle: Bundle? = null
) {
    requireContext().startActivity<T>(finishPrevious, bundle)
}

inline fun <reified T : Parcelable> Activity.getParcelable(extra: String) = lazy {
    intent.getParcelableExtra<T>(extra)
}

fun String.toUri(): Uri = Uri.parse(this)

fun String.toBase64(): String {
    val data = toByteArray(StandardCharsets.UTF_8)
    return Base64.encodeToString(data, Base64.DEFAULT)
}

fun String.fromBase64(): String {
    val data = Base64.decode(this, Base64.DEFAULT)
    return String(data, StandardCharsets.UTF_8)
}


fun Fragment.finish() {
    activity?.finish()
}

fun Fragment.navTo(directions: NavDirections) {
    findNavController().navigate(directions)
}

fun Fragment.navTo(@IdRes id: Int) {
    findNavController().navigate(id)
}

fun FragmentActivity.startActionView(url: String, extension: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            val mimeType = getMimeType(extension)
            if (mimeType.isNullOrEmpty()) {
                toast(getString(R.string.url_invalid_try_download), Toast.LENGTH_LONG)
            }
            setDataAndType(url.toUri(), mimeType)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
        toast(getString(R.string.url_invalid_try_download), Toast.LENGTH_LONG)
    }
}

fun getMimeType(extension: String): String? {
    return when (extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "pdf" -> "application/pdf"
        "txt" -> "text/plain"
        else -> null
    }
}

