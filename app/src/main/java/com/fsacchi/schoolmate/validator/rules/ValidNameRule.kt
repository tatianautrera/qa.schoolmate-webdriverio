package com.fsacchi.schoolmate.validator.rules

import com.fsacchi.schoolmate.core.extensions.isValidName
import com.fsacchi.schoolmate.validator.Event
import com.fsacchi.schoolmate.validator.Rule
import com.fsacchi.schoolmate.validator.extension.text
import com.google.android.material.textfield.TextInputLayout

class ValidNameRule(
    val allowNumbers: Boolean = false,
    view: TextInputLayout,
    errorMessage: String
) : Rule<TextInputLayout>(view, errorMessage) {

    override val events: List<Event>
        get() = listOf(Event.ON_FOCUS_CHANGE, Event.ON_TEXT_CHANGE)

    override fun isValid(view: TextInputLayout): Boolean {
        return view.text.isValidName(allowNumbers)
    }
}
