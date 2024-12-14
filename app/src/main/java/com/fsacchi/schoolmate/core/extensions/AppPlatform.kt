package com.fsacchi.schoolmate.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Base64
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
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

