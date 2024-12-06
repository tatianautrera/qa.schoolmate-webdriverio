package com.fsacchi.schoolmate.core.features.login

import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.fsacchi.schoolmate.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val layoutRes: Int get() = R.layout.activity_login
    private val firebaseAuth: FirebaseAuth by inject()

    override fun init() {
        firebaseAuth.setLanguageCode("pt")
    }

    fun showAlertMessage(title: String, message: String? = "", isError: Boolean) {
        binding.alertCard.showAlert(title, message, isError)
    }
}