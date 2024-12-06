package com.fsacchi.schoolmate.validator.rules

import com.fsacchi.schoolmate.core.extensions.isValidDate
import com.fsacchi.schoolmate.validator.Event
import com.fsacchi.schoolmate.validator.InputType
import com.fsacchi.schoolmate.validator.Rule
import com.fsacchi.schoolmate.validator.extension.message
import com.fsacchi.schoolmate.validator.extension.text
import com.google.android.material.textfield.TextInputLayout

class DateRule(
    val isOptional: Boolean,
    private val pattern: String,
    view: TextInputLayout,
    errorMessage: String
) : Rule<TextInputLayout>(view, errorMessage) {

    override val events: List<Event>
        get() = listOf(Event.ON_FOCUS_CHANGE, Event.ON_TEXT_CHANGE)

    override fun isValid(view: TextInputLayout): Boolean {
        return if (isOptional && view.text.isEmpty()) {
            true
        } else {
            view.text.length == pattern.length && view.text.isValidDate(pattern)
        }
    }

    override fun onValidationFailed(view: TextInputLayout, event: Event) {
        if (isOptional && view.text.isEmpty()) {
            return
        }

        if (view.text.length != pattern.length && event == Event.ON_FOCUS_CHANGE) {
            if (view.text.isNotEmpty() && !this.view.text.isValidDate()) {
                this.message = view.message(InputType.Date.message)
            }

            super.onValidationFailed(view, event)
        } else if (view.text.length == pattern.length && !this.view.text.isValidDate()) {
            super.onValidationFailed(view, event)
        }
    }
}
