package com.fsacchi.schoolmate.validator.rules

import com.fsacchi.schoolmate.validator.Event
import com.fsacchi.schoolmate.validator.Rule
import com.fsacchi.schoolmate.validator.extension.text
import com.google.android.material.textfield.TextInputLayout

class MinRule(
    val length: Int,
    view: TextInputLayout,
    errorMessage: String
) : Rule<TextInputLayout>(view, errorMessage) {

    override val events: List<Event>
        get() = listOf(Event.ON_FOCUS_CHANGE, Event.ON_TEXT_CHANGE)

    override fun isValid(view: TextInputLayout): Boolean {
        return view.text.length >= length
    }

    override fun onValidationFailed(view: TextInputLayout, event: Event) {
        if (view.text.length != length && event != Event.ON_FOCUS_CHANGE) return

        super.onValidationFailed(view, event)
    }
}
