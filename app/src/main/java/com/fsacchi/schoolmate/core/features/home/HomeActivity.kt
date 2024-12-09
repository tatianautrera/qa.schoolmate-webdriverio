package com.fsacchi.schoolmate.core.features.home

import android.support.annotation.StringRes
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.BottomBar
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.databinding.ActivityHomeBinding
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val homeViewModel: HomeViewModel by inject()
    private val dialog by lazy { createProgressDialog() }
    private var menuSelected: ((BottomBar.MenuBottom) -> Unit)? = null

    override val layoutRes: Int get() = R.layout.activity_home
    lateinit var user: UserEntity

    override fun init() {
        binding.customToolbar.showBackIcon(false)
        observe()
        insertListeners()
    }

    fun menuSelected(listener: (BottomBar.MenuBottom) -> Unit) {
        menuSelected = listener
    }

    private fun updateTitleMenu(menuBottom: BottomBar.MenuBottom) {
        val menu = when(menuBottom) {
            BottomBar.MenuBottom.DISCIPLINE -> getString(R.string.discipline)
            BottomBar.MenuBottom.AGENDA -> getString(R.string.agenda)
            BottomBar.MenuBottom.FILE -> getString(R.string.files)
        }
        binding.customToolbar.setTitle(menu)
    }

    private fun insertListeners() {
        binding.bottomBar.setListener {
            updateTitleMenu(it)
            menuSelected?.invoke(it)
        }
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
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_calendar -> {
                true
            }
            R.id.action_logoff -> {
                homeViewModel.logoff()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showAlertMessage(title: String, message: String? = "", isError: Boolean) {
        binding.alertCard.showAlert(title, message, isError)
    }

    fun setLoggedUser(userEntity: UserEntity?) {
        userEntity?.let {
            user = userEntity
        }
    }



}