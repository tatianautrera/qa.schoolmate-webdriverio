package com.fsacchi.schoolmate.core.features.login

import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.databinding.FragmentRegisterUserBinding
import com.fsacchi.schoolmate.model.login.RegisterUserModel
import com.fsacchi.schoolmate.validator.Validator

class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding>() {

    private lateinit var validator: Validator
    override val layoutRes: Int
        get() = R.layout.fragment_register_user

    override fun start() {
        binding.item = RegisterUserModel()

        insertListeners()
        setupValidation()
    }

    private fun setupValidation() {
        validator = Validator(binding)
        listOf(
            binding.tilName, binding.tilEmail, binding.tilCreatePassword, binding.tilConfirmPassword
        ).forEach {
            validator.observe(it)
        }

        validator.validateListener = {
            binding.btnCreateAccount.enable(it)
        }

        validator.validateNow()
    }

    private fun insertListeners() = with(binding) {
        btnCancel.clickListener {
            popBackStack()
        }
        btnCreateAccount.clickListener{
            binding.item?.let {
                if(it.password != it.confirmPassword) {
                    (activity as LoginActivity).showAlertMessage(
                        isError = true,
                        title = "Erro de validação",
                        message = "As senhas não coincidem"
                    )
                }
            }
        }
    }
}
