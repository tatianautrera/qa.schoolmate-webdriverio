package com.fsacchi.schoolmate.core.features.login

import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.databinding.FragmentForgotPasswordBinding
import com.fsacchi.schoolmate.presentation.features.LoginViewModel
import com.fsacchi.schoolmate.presentation.states.LoginUiState
import com.fsacchi.schoolmate.validator.Validator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    private lateinit var validator: Validator
    private val viewModel: LoginViewModel by viewModel()
    private val dialog by lazy { createProgressDialog() }

    override val layoutRes: Int
        get() = R.layout.fragment_forgot_password

    override fun start() {
        binding.item = viewModel.userModel
        lifecycle.addObserver(viewModel)

        insertListeners()
        setupValidation()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.uiState.login.collect { loginUiState ->
                loginUiState?.let {
                    when(it.screenType) {
                        is LoginUiState.ScreenType.Error -> {
                            dialog.dismiss()
                            (activity as LoginActivity).showAlertMessage(
                                isError = true,
                                title = it.screenType.errorTitle ?: "Erro ao enviar email",
                                message = it.screenType.errorMessage.orEmpty()
                            )
                        }
                        LoginUiState.ScreenType.Loading -> {
                            dialog.show()
                        }
                        LoginUiState.ScreenType.Success -> {
                            dialog.dismiss()
                            (activity as LoginActivity).showAlertMessage(
                                isError = false,
                                title = "Email enviado",
                                message = "Altere sua senha através do email, para acessar o Schoolmate"
                            )
                            popBackStack()
                        }
                    }
                }
            }
        }
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

        btnSendLink.clickListener {
            viewModel.forgotPassword()
        }
    }
}
