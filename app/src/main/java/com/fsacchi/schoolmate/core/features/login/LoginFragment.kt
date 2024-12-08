package com.fsacchi.schoolmate.core.features.login

import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.extensions.navTo
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.home.HomeActivity
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding
import com.fsacchi.schoolmate.presentation.features.LoginViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.validator.Validator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var validator: Validator
    private val viewModel: LoginViewModel by viewModel()
    private val dialog by lazy { createProgressDialog() }

    override val layoutRes: Int
        get() = R.layout.fragment_login

    override fun start() {
        viewModel.updateLoginSaved()

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
                        is DefaultUiState.ScreenType.Error -> {
                            dialog.dismiss()
                            (activity as LoginActivity).showAlertMessage(
                                isError = true,
                                title = it.screenType.errorTitle ?: "Erro ao fazer login",
                                message = it.screenType.errorMessage.orEmpty()
                            )
                        }
                        DefaultUiState.ScreenType.Loading -> {
                            dialog.show()
                        }
                        DefaultUiState.ScreenType.Success -> {
                            dialog.dismiss()
                            startActivity<HomeActivity>(finishPrevious = true)
                        }
                    }
                }
            }
        }
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
            viewModel.login()
        }
        tvForgotPassword.clickListener {
            navTo(LoginFragmentDirections.goToForgotPassword())
        }
        btnRegister.clickListener {
            navTo(LoginFragmentDirections.goToRegisterUser())
        }
    }
}
