package com.fsacchi.schoolmate.validator

import androidx.annotation.StringRes
import com.fsacchi.schoolmate.R

enum class InputType(@StringRes val message: Int) {
    Empty(R.string.msg_error_field_mandatory),
    Cpf(R.string.msg_invalid_cpf),
    Cnpj(R.string.msg_invalid_cnpj),
    CellPhone(R.string.msg_invalid_phone),
    Checked(R.string.empty),
    ZipCode(R.string.msg_invalid_zipcode),
    CompleteName(R.string.msg_complete_name),
    Name(R.string.msg_name_invalid),
    NameIgnoreNumber(R.string.msg_name_ignore_number_invalid),
    Date(R.string.msg_invalid_date),
    DateAfterNow(R.string.msg_invalid_date_big_today),
    Majority(R.string.msg_invalid_date_majority),
    Email(R.string.msg_invalid_email),
    MinValue(R.string.msg_invalid_value),
    WebSite(R.string.msg_invalid_url)
}
