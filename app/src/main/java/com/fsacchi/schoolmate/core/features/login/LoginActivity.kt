package com.fsacchi.schoolmate.core.features.login

import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.fsacchi.schoolmate.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val layoutRes: Int get() = R.layout.activity_login

    override fun init() {}

    fun showAlertMessage(title: String, message: String? = "", isError: Boolean) {
        binding.alertCard.showAlert(title, message, isError)
    }
}