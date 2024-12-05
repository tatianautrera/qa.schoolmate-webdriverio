package com.fsacchi.schoolmate.core.features.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.fsacchi.schoolmate.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val layoutRes: Int get() = R.layout.activity_splash

    override fun init() {
        val fadeAnimation = ObjectAnimator.ofFloat(binding.icCheck, "alpha", 0f, 1f).apply {
            duration = 2500
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    startActivity<LoginActivity>(true)
                }
            })
        }
        fadeAnimation.start()
    }
}
