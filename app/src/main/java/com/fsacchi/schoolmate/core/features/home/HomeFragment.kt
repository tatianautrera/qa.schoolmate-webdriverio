package com.fsacchi.schoolmate.core.features.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.BottomBar
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.navTo
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

    private lateinit var homeActivity: HomeActivity
    private val homeViewModel: HomeViewModel by inject()
    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun start() {
        homeActivity = (activity as HomeActivity)
        homeViewModel.getUser()
        observe()
        insertListeners()
    }

    private fun observe() {
        lifecycleScope.launch {
            homeViewModel.uiState.user.collect { userUiState ->
                userUiState?.let {
                    when(it.screenType) {
                        UserUiState.ScreenType.Await -> {}
                        is UserUiState.ScreenType.Loaded -> {
                            (activity as HomeActivity).setLoggedUser(it.screenType.userEntity)
                        }
                    }
                }
            }
        }
    }

    private fun insertListeners() {
        homeActivity.menuSelected {
            when(it) {
                BottomBar.MenuBottom.DISCIPLINE -> {
                    navTo(HomeFragmentDirections.goToDiscipline())
                }
                BottomBar.MenuBottom.AGENDA -> {}
                BottomBar.MenuBottom.FILE -> {
                    navTo(HomeFragmentDirections.goToFile())
                }
            }
        }
    }
}
