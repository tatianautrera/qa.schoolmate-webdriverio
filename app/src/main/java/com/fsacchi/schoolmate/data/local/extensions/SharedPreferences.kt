package com.fsacchi.schoolmate.data.local.extensions

import android.content.SharedPreferences

fun SharedPreferences.put(key: String, value: Any) = apply {
    edit().apply {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Boolean -> putBoolean(key, value)
            is Enum<*> -> putString(key, value.name)
        }
    }.apply()
}

fun SharedPreferences
        .string(key: String) = getString(key, "")
fun SharedPreferences.int(key: String) = getInt(key, -1)

fun SharedPreferences.bool(key: String) = getBoolean(key, false)
fun SharedPreferences.long(key: String) = getLong(key, -1)
