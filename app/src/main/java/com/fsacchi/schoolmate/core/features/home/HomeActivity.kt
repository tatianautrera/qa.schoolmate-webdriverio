package com.fsacchi.schoolmate.core.features.home

import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.databinding.ActivityHomeBinding
import com.fsacchi.schoolmate.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override val layoutRes: Int get() = R.layout.activity_home
    lateinit var user: UserEntity

    override fun init() {}

    fun showAlertMessage(title: String, message: String? = "", isError: Boolean) {
        binding.alertCard.showAlert(title, message, isError)
    }

    fun setLoggedUser(userEntity: UserEntity?) {
        userEntity?.let {
            user = userEntity
        }
    }

}