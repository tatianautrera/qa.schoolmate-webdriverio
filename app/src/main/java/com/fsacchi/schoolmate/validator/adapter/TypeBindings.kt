package com.fsacchi.schoolmate.validator.adapter

import android.widget.CheckBox
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.fsacchi.schoolmate.validator.InputType
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.DateMasks
import com.fsacchi.schoolmate.validator.extension.appendValue
import com.fsacchi.schoolmate.validator.extension.disableErrorOnChanged
import com.fsacchi.schoolmate.validator.extension.message
import com.fsacchi.schoolmate.validator.rules.CheckedRule
import com.fsacchi.schoolmate.validator.rules.DateRule
import com.fsacchi.schoolmate.validator.rules.EmailRule
import com.fsacchi.schoolmate.validator.rules.EmptyRule
import com.fsacchi.schoolmate.validator.rules.MinRule
import com.fsacchi.schoolmate.validator.rules.NameCompleteRule
import com.fsacchi.schoolmate.validator.rules.RangeRule
import com.fsacchi.schoolmate.validator.rules.ValidNameRule
import com.google.android.material.textfield.TextInputLayout as Input

object TypeBindings {

    //region type bindings

    @JvmStatic
    @BindingAdapter(value = ["app:validate_empty", "app:empty_message", "app:validate_empty_optional"], requireAll = false)
    fun empty(view: Input, type: InputType, message: String?, optional: Boolean) {
        view.disableErrorOnChanged()

        val errorMessage = view.message(message, type.message)
        view.appendValue(R.id.validator_rule, EmptyRule(view, errorMessage, optional))
    }

    @JvmStatic
    @BindingAdapter("app:validate_check")
    fun check(view: CheckBox, ignored: InputType) {
        view.appendValue(R.id.validator_rule, CheckedRule(view, ""))
    }

    @JvmStatic
    @BindingAdapter(value = ["app:validate_min", "app:validate_min_message"], requireAll = false)
    fun minLength(view: Input, length: Int, message: String?) {
        view.disableErrorOnChanged()

        val defaultMessage = view.context.getString(R.string.msg_min_length, length)
        val errorMessage = if (message.isNullOrEmpty()) defaultMessage else message

        view.appendValue(R.id.validator_rule, MinRule(length, view, errorMessage))
    }

    @JvmStatic
    @BindingAdapter(
        value = ["app:validate_range_min", "app:validate_range_max", "app:validate_range_message", "app:validate_range_is_only_digit"],
        requireAll = false
    )
    fun rangeLength(view: Input, min: Int, max: Int, message: String?, isOnlyDigit: Boolean = false) {
        view.disableErrorOnChanged()

        val stringRes = if (isOnlyDigit) R.string.msg_range_length_digit else R.string.msg_range_length
        val defaultMessage = view.context.getString(stringRes, min, max)

        val errorMessage = if (message.isNullOrEmpty()) defaultMessage else message

        view.appendValue(
            R.id.validator_rule,
            RangeRule(min_length = min, max_length = max, view = view, errorMessage = errorMessage)
        )
    }

    @JvmStatic
    @BindingAdapter(value = ["app:validate_email", "app:validate_email_message", "app:validate_email_optional"], requireAll = false)
    fun email(view: Input, type: InputType, message: String?, optional: Boolean) {
        view.disableErrorOnChanged()

        val errorMessage = view.message(message, type.message)
        view.appendValue(R.id.validator_rule, EmailRule(view, errorMessage, optional))
    }

    //region custom bindings

    @JvmStatic
    @BindingAdapter(value = ["app:validate_date", "app:date_optional", "app:date_message"], requireAll = false)
    fun bindingDate(
        view: Input,
        pattern: String = DateMasks.appFormat,
        isOptional: Boolean = false,
        message: String?
    ) {
        view.disableErrorOnChanged()

        val errorMessage = view.message(message, R.string.msg_invalid_date)
        view.appendValue(R.id.validator_rule, DateRule(isOptional, pattern, view, errorMessage))
    }

    @JvmStatic
    @BindingAdapter(value = ["app:validate_name_complete", "app:name_complete_message"], requireAll = false)
    fun bindingNameComplete(view: Input, type: InputType, message: String?) {
        view.disableErrorOnChanged()

        val errorMessage = view.message(message, type.message)
        view.appendValue(R.id.validator_rule, NameCompleteRule(view, errorMessage))
    }

    @JvmStatic
    @BindingAdapter(value = ["app:validate_name", "app:name_message", "app:name_allow_number"], requireAll = false)
    fun bindingValidName(view: Input, type: InputType, message: String?, allowNumber: Boolean) {
        view.disableErrorOnChanged()

        val errorMessage = view.message(message, type.message)
        view.appendValue(R.id.validator_rule, ValidNameRule(allowNumber, view, errorMessage))
    }
    //endregion
}
