package com.fsacchi.schoolmate.core.features.login

import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.extensions.navTo
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding
import com.fsacchi.schoolmate.validator.Validator

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var validator: Validator
    override val layoutRes: Int
        get() = R.layout.fragment_login

    override fun start() {
        insertListeners()
        setupValidation()
    }

    private fun setupValidation() {
        validator = Validator(binding)
        listOf(
            binding.tilEmail, binding.tilPassword
        ).forEach {
            validator.observe(it)
        }

        validator.validateListener = {
            binding.btnEnter.enable(it)
        }

        validator.validateNow()
    }

    private fun insertListeners() = with(binding) {
        btnEnter.clickListener {
            activity?.let {
                (it as LoginActivity).showAlertMessage(title = "Algo deu errado", message = "Verifique sua internet",  isError = true)
            }
        }
        tvForgotPassword.clickListener {
            navTo(LoginFragmentDirections.goToForgotPassword())
        }
        btnRegister.clickListener {
            navTo(LoginFragmentDirections.goToRegisterUser())
        }
    }
}
