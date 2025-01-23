package com.fsacchi.schoolmate.core.features.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.home.HomeActivity
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.platform.BaseActivity
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
        checkPermissionAlarm()
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

    @SuppressLint("ServiceCast")
    private fun checkPermissionAlarm() {
        try {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !alarmManager.canScheduleExactAlarms()) {
                Log.e("App", "Permissão para alarmes exatos não concedida.")
            }
        } catch (e: SecurityException) {
            Log.e("App", "Erro ao verificar permissões para alarmes exatos", e)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            homeViewModel.uiState.user.collect { userUiState ->
                userUiState?.let {
                    when(it.screenType) {
                        UserUiState.ScreenType.Await -> {}
                        is UserUiState.ScreenType.Loaded -> {
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
