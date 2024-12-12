package com.fsacchi.schoolmate.core.extensions

import android.os.Bundle
import android.os.Parcelable
import kotlin.reflect.KProperty

fun <T : Any> fragArgs(key: String, bundle: Bundle) = BundleDelegate<T>(key, bundle)

class BundleDelegate<T : Any>(private val key: String, private val bundle: Bundle) {

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any, property: KProperty<*>): T? =
        if (bundle.containsKey(key)) bundle[key] as T
        else null

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        bundle.put(key, value)
    }
}

fun <T> Bundle.put(key: String, value: T?) = apply {
    when (value) {
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Boolean -> putBoolean(key, value)
        is Parcelable -> putParcelable(key, value)
        else -> throw RuntimeException("not implemented")
    }
}
