package com.fsacchi.schoolmate.validator

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.widget.doOnTextChanged
import androidx.databinding.ViewDataBinding
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.validator.extension.Input
import com.fsacchi.schoolmate.validator.extension.getViewsByTag
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Validator(private val binding: ViewDataBinding) {

    var validateListener: ((Boolean) -> Unit)? = null

    private val viewsWithValidation: List<View>
        get() = if (binding.root is ViewGroup) {
            (binding.root as ViewGroup).getViewsByTag(R.id.validator_rule)
        } else {
            listOf(binding.root)
        }

    fun getViews() = viewsWithValidation

    fun validateNow() {
        validateAllViews(Event.ON_FOCUS_CHANGE)
    }

    fun isValid(view: View): Boolean {
        return view.getRules().validateRules(null)
    }

    fun observe(listener: (Boolean) -> Unit) = apply {
        GlobalScope.launch {
            delay(100)
            insertListeners()
        }

        validateListener = listener
    }

    fun <View> observe(view: View, observe: ((View, Boolean, Event) -> Unit)? = null) {
        if (view is Input) {
            val events = view.getRules().flatMap { it.events }.toSet()

            if (Event.ON_FOCUS_CHANGE in events) {
                view.editText?.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        observe?.invoke(view, validate(view, null), Event.ON_FOCUS_CHANGE)
                            ?: validateAllViews(Event.ON_FOCUS_CHANGE)
                    }
                }
            }

            if (Event.ON_TEXT_CHANGE in events) {
                view.editText?.doOnTextChanged { _, _, _, _ ->
                    observe?.invoke(view, validate(view, null), Event.ON_TEXT_CHANGE)
                        ?: validateAllViews(Event.ON_TEXT_CHANGE)
                }
            }
        }
        if (view is CheckBox) {
            view.setOnCheckedChangeListener { _, _ ->
                observe?.invoke(view, validate(view, null), Event.ON_TEXT_CHANGE)
                    ?: validateAllViews(Event.ON_CHECKED)
            }
        }
    }

    private fun insertListeners() {
        viewsWithValidation
            .mapNotNull { it as? CheckBox }
            .forEach {
                it.setOnCheckedChangeListener { _, _ ->
                    validateAllViews(Event.ON_CHECKED)
                }
            }

        viewsWithValidation
            .mapNotNull { it as? Input }
            .forEach { input ->
                val events = input.getRules().flatMap { it.events }.toSet()
                if (Event.ON_FOCUS_CHANGE in events) {
                    input.editText?.setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) validateAllViews(Event.ON_FOCUS_CHANGE)
                    }
                }

                if (Event.ON_TEXT_CHANGE in events) {
                    input.editText?.doOnTextChanged { _, _, _, _ ->
                        validateAllViews(Event.ON_TEXT_CHANGE)
                    }
                }
            }
    }

    private fun validate(view: View, event: Event?): Boolean {
        return view.getRules()
            .validateRules(event)
    }

    fun validateAllViews(event: Event? = null) {
        val isValid = viewsWithValidation.map { validate(it, event) }
            .reduce { acc, current ->
                acc and current
            }
        validateListener?.invoke(isValid)
    }

    private fun List<Rule<*>>.validateRules(event: Event?): Boolean {
        return fold(true) { acc, current ->
            if (event == null) acc && current.validate()
            else acc && current.validate(event)
        }
    }

    private fun View.getRules(): List<Rule<*>> {
        return getTag(R.id.validator_rule) as List<Rule<*>>
    }
}
