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
import com.fsacchi.schoolmate.core.features.home.sheets.DisciplineBottomSheet
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.features.splash.SplashActivity
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.databinding.FragmentDisciplineBinding
import com.fsacchi.schoolmate.databinding.FragmentHomeBinding
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DisciplineFragment : BaseFragment<FragmentDisciplineBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_discipline

    override fun start() {
        insertListeners()
    }

    private fun insertListeners() {
        (activity as HomeActivity).menuSelected {
            when(it) {
                BottomBar.MenuBottom.DISCIPLINE -> {}
                BottomBar.MenuBottom.AGENDA -> {
                    navTo(DisciplineFragmentDirections.goToHome())
                }
                BottomBar.MenuBottom.FILE -> {
                    navTo(DisciplineFragmentDirections.goToFile())
                }
            }
        }

        binding.btnCreateDiscipline.clickListener {
            DisciplineBottomSheet.newInstance(DisciplineModel()) {
                val disciplineModel = it
            }.show(childFragmentManager)
        }
    }
}
