package com.fsacchi.schoolmate.core.features.home

import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.features.splash.SplashActivity
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.databinding.FragmentHomeBinding
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by inject()
    private val dialog by lazy { createProgressDialog() }
    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun start() {
        homeViewModel.getUser()
        observe()
        insertListeners()
    }

    private fun observe() {
        lifecycleScope.launch {
            homeViewModel.uiState.logoff.collect { logoffUiState ->
                logoffUiState?.let {
                    when(it.screenType) {
                        is DefaultUiState.ScreenType.Error -> {}
                        DefaultUiState.ScreenType.Loading -> {
                            dialog.show()
                        }
                        DefaultUiState.ScreenType.Success -> {
                            dialog.dismiss()
                            startActivity<LoginActivity>(finishPrevious = true)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            homeViewModel.uiState.user.collect { userUiState ->
                userUiState?.let {
                    when(it.screenType) {
                        UserUiState.ScreenType.Await -> {}
                        is UserUiState.ScreenType.Loaded -> {
                            (activity as HomeActivity).setLoggedUser(it.screenType.userEntity)
                            binding.tvNameUse.text = it.screenType.userEntity?.name
                        }
                    }
                }
            }
        }
    }

    private fun insertListeners() {
        binding.btnLogout.clickListener {
            homeViewModel.logoff()
        }
    }
}
