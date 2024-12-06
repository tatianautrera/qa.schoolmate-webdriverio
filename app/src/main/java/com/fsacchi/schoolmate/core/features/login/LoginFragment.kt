package com.fsacchi.schoolmate.core.features.login

import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.navTo
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_login

    override fun start() {
        insertListeners()
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
