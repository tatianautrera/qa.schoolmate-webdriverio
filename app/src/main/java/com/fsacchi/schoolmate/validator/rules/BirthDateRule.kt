package com.fsacchi.schoolmate.validator.rules

import com.fsacchi.schoolmate.core.extensions.isValidDate
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.core.extensions.resetTime
import com.fsacchi.schoolmate.core.extensions.toDate
import com.fsacchi.schoolmate.core.extensions.verifyMaiority
import com.fsacchi.schoolmate.validator.Event
import com.fsacchi.schoolmate.validator.InputType
import com.fsacchi.schoolmate.validator.Rule
import com.fsacchi.schoolmate.validator.extension.message
import com.fsacchi.schoolmate.validator.extension.text
import com.google.android.material.textfield.TextInputLayout

class BirthDateRule(
    private val verifyMajority: Boolean,
    view: TextInputLayout,
    errorMessage: String
) : Rule<TextInputLayout>(view, errorMessage) {

    override val events: List<Event>
        get() = listOf(Event.ON_TEXT_CHANGE, Event.ON_FOCUS_CHANGE)

    override fun isValid(view: TextInputLayout): Boolean {
        val isValid = view.text.length == 10 && view.text.isValidDate() && view.text.toDate().resetTime().before(now())
        return if (verifyMajority) isValid && view.text.toDate().verifyMaiority()
        else isValid
    }

    override fun onValidationFailed(view: TextInputLayout, event: Event) {
        if (this.view.text.length < 10) {
            if (event == Event.ON_FOCUS_CHANGE) {
                message = message(InputType.Date)
                super.onValidationFailed(view, event)
            }
            return
        }

        message = when {
            !view.text.isValidDate() -> message(InputType.Date)
            !view.text.toDate().resetTime().before(now()) && view.text.isValidDate() -> message(
                InputType.DateAfterNow)
            verifyMajority && view.text.isValidDate() -> message(InputType.Majority)
            else -> ""
        }

        if (event == Event.ON_FOCUS_CHANGE) {
            super.onValidationFailed(view, event)
            return
        }

        super.onValidationFailed(view, event)
    }

    private fun message(type: InputType) = view.message(type.message)
}
