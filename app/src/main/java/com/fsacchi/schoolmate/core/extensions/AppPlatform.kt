package com.fsacchi.schoolmate.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.fsacchi.schoolmate.core.platform.BaseActivity

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

inline fun <reified T : BaseActivity<*>> Fragment.startActivity(finishPrevious: Boolean = false) {
    requireContext().startActivity<T>(finishPrevious)
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

fun Fragment.finish() {
    activity?.finish()
}

