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
import com.fsacchi.schoolmate.databinding.FragmentDisciplineBinding
import com.fsacchi.schoolmate.databinding.FragmentFileBinding
import com.fsacchi.schoolmate.databinding.FragmentHomeBinding
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FilesFragment : BaseFragment<FragmentFileBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_file

    override fun start() {
        insertListeners()
    }

    private fun insertListeners() {
        (activity as HomeActivity).menuSelected {
            when(it) {
                BottomBar.MenuBottom.DISCIPLINE -> {
                    navTo(FilesFragmentDirections.goToDiscipline())
                }
                BottomBar.MenuBottom.AGENDA -> {
                    navTo(FilesFragmentDirections.goToHome())
                }
                BottomBar.MenuBottom.FILE -> {}
            }
        }
    }
}
