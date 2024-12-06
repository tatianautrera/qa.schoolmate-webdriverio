package com.fsacchi.schoolmate.validator.rules

import com.fsacchi.schoolmate.core.extensions.isEmailValid
import com.fsacchi.schoolmate.validator.Event
import com.fsacchi.schoolmate.validator.Rule
import com.fsacchi.schoolmate.validator.extension.Input
import com.fsacchi.schoolmate.validator.extension.text
import com.google.android.material.textfield.TextInputLayout

class EmailRule(
    view: TextInputLayout,
    errorMessage: String,
    val optional: Boolean
) : Rule<Input>(view, errorMessage) {

    override val events: List<Event>
        get() = listOf(Event.ON_FOCUS_CHANGE, Event.ON_TEXT_CHANGE)

    override fun isValid(view: Input): Boolean {
        if (optional && view.text.isEmpty()) return true

        return view.text.isEmailValid()
    }

    override fun onValidationFailed(view: Input, event: Event) {
        if (view.text.length < SIZE_VALIDATION && event == Event.ON_TEXT_CHANGE) return
        super.onValidationFailed(view, event)
    }

    companion object {
        private const val SIZE_VALIDATION = 7
    }
}
