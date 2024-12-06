package com.fsacchi.schoolmate.validator.rules

import com.fsacchi.schoolmate.validator.Event
import com.fsacchi.schoolmate.validator.Rule
import com.fsacchi.schoolmate.validator.extension.text
import com.google.android.material.textfield.TextInputLayout

class EmptyRule(view: TextInputLayout, message: String, val optional: Boolean) : Rule<TextInputLayout>(view, message) {

    override val events: List<Event>
        get() = listOf(Event.ON_FOCUS_CHANGE, Event.ON_TEXT_CHANGE)

    override fun isValid(view: TextInputLayout): Boolean {
        if (optional && view.text.isEmpty()) return true
        return view.text.isNotBlank()
    }
}
