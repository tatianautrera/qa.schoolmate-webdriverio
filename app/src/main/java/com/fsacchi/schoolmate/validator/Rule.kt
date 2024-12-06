package com.fsacchi.schoolmate.validator

import androidx.core.view.isGone
import com.fsacchi.schoolmate.validator.extension.Input
import com.fsacchi.schoolmate.validator.extension.putError
import com.fsacchi.schoolmate.validator.extension.removeError
import com.fsacchi.schoolmate.validator.extension.text

abstract class Rule<View>(val view: View, var message: String) {

    abstract val events: List<Event>

    fun validate(event: Event) = startValidation().also {
        if (it) onValidationSucceeded(view, event)
        else onValidationFailed(view, event)
    }

    fun validate() = startValidation()

    private fun startValidation() = if (bypass()) true else isValid(view)

    private fun bypass() = (view as? android.view.View)?.isGone ?: false

    protected abstract fun isValid(view: View): Boolean

    open fun onValidationSucceeded(view: View, event: Event) {
        if (view is Input) view.removeError()
    }

    open fun onValidationFailed(view: View, event: Event) {
        if (view is Input && view.text.isNotEmpty()) view.putError(message)
    }
}
