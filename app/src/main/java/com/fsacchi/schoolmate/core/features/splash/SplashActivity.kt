package com.fsacchi.schoolmate.core.features.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.home.HomeActivity
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.fsacchi.schoolmate.core.platform.worker.checkAndScheduleJob
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.databinding.ActivitySplashBinding
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private var animationCompleted = false
    private var userLoaded = false
    private var user: UserEntity? = null
    private val homeViewModel: HomeViewModel by inject()
    override val layoutRes: Int get() = R.layout.activity_splash

    override fun init() {
        homeViewModel.getUser()
        observe()

        val fadeAnimation = ObjectAnimator.ofFloat(binding.icCheck, "alpha", 0f, 1f).apply {
            duration = 2500
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animationCompleted = true
                }
            })
        }
        fadeAnimation.start()
    }

    private fun observe() {
        lifecycleScope.launch {
            homeViewModel.uiState.user.collect { userUiState ->
                userUiState?.let {
                    when(it.screenType) {
                        UserUiState.ScreenType.Await -> {}
                        is UserUiState.ScreenType.Loaded -> {
                            checkAndScheduleJob(applicationContext)
                            userLoaded = true
                            user = it.screenType.userEntity
                            nextRoute()
                        }
                    }
                }
            }
        }
    }

    private fun nextRoute() {
        lifecycleScope.launch {
            while (!(userLoaded && animationCompleted)) {
                delay(100)
            }

            if (user != null) {
                startActivity<HomeActivity>(true)
            } else {
                startActivity<LoginActivity>(true)
            }
        }
    }
}
