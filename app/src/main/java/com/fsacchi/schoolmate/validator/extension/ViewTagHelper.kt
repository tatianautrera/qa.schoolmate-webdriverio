package com.fsacchi.schoolmate.validator.extension

import android.view.View
import android.view.ViewGroup

fun <Type> View.appendValue(tagId: Int, value: Type) {
    when (val tag = getTag(tagId)) {
        is List<*> -> (tag as? MutableList<Type>)?.add(value)
        else -> setTag(tagId, arrayListOf<Type>().apply { add(value) })
    }
}

fun ViewGroup.getViewsByTag(tagId: Int): List<View> {
    val views: MutableList<View> = ArrayList()
    (0 until childCount).forEach { i ->
        val child = getChildAt(i)
        if (child is ViewGroup) views.addAll(child.getViewsByTag(tagId))
        child.getTag(tagId)?.let { views.add(child) }
    }
    return views
}
