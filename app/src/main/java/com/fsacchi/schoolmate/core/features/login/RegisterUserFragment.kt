package com.fsacchi.schoolmate.core.features.login

import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.databinding.FragmentForgotPasswordBinding
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding
import com.fsacchi.schoolmate.databinding.FragmentRegisterUserBinding

class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_register_user

    override fun start() {
        insertListeners()
    }

    private fun insertListeners() = with(binding) {
        btnCancel.clickListener {
            popBackStack()
        }
    }
}
