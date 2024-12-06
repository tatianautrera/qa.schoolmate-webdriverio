package com.fsacchi.schoolmate.core.features.login

import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.databinding.FragmentForgotPasswordBinding
import com.fsacchi.schoolmate.validator.Validator

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    private lateinit var validator: Validator

    override val layoutRes: Int
        get() = R.layout.fragment_forgot_password

    override fun start() {
        insertListeners()
        setupValidation()
    }

    private fun setupValidation() {
        validator = Validator(binding)
        listOf(
            binding.tilEmail
        ).forEach {
            validator.observe(it)
        }

        validator.validateListener = {
            binding.btnSendLink.enable(it)
        }

        validator.validateNow()
    }

    private fun insertListeners() = with(binding) {
        btnCancel.clickListener {
            popBackStack()
        }
    }
}
