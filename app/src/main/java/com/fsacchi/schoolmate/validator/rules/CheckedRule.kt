package com.fsacchi.schoolmate.validator.rules

import android.widget.CheckBox
import com.fsacchi.schoolmate.validator.Event
import com.fsacchi.schoolmate.validator.Rule

class CheckedRule(view: CheckBox, message: String) : Rule<CheckBox>(view, message) {

    override val events: List<Event>
        get() = listOf(Event.ON_CHECKED)

    override fun isValid(view: CheckBox): Boolean {
        return view.isChecked
    }
}
