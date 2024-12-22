package com.fsacchi.schoolmate.core.features.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.BottomBar
import com.fsacchi.schoolmate.core.components.CalendarDialog
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.features.utils.ConfigActivity
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.databinding.ActivityHomeBinding
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Date

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    companion object {
        const val REQUEST_CODE_POST_NOTIFICATIONS = 1001
    }

    private val homeViewModel: HomeViewModel by inject()
    private val dialog by lazy { createProgressDialog() }
    private var menuSelected: ((BottomBar.MenuBottom) -> Unit)? = null
    private var dateListener: ((Date) -> Unit)? = null

    var dateSelected: Date? = null

    var listenerBack: () -> Unit = {}

    override val layoutRes: Int get() = R.layout.activity_home
    lateinit var user: UserEntity


    override fun init() {
        showBackIcon(false)
        observe()
        insertListeners()
        requestPermissionPush()
    }

    fun menuSelected(listener: (BottomBar.MenuBottom) -> Unit) {
        menuSelected = listener
    }

    fun dateListener(listener: (Date) -> Unit) {
        dateListener = listener
    }

    private fun updateTitleMenu(menuBottom: BottomBar.MenuBottom) {
        val menu = when(menuBottom) {
            BottomBar.MenuBottom.DISCIPLINE -> getString(R.string.discipline)
            BottomBar.MenuBottom.AGENDA -> getString(R.string.agenda)
            BottomBar.MenuBottom.FILE -> getString(R.string.files)
        }
        binding.customToolbar.setTitle(menu)
    }

    fun setCustomTitle(title: String) {
        binding.customToolbar.setTitle(title)
    }

    fun showBackIcon(show: Boolean) {
        binding.customToolbar.showBackIcon(show)
    }

    private fun insertListeners() {
        binding.bottomBar.setListener {
            updateTitleMenu(it)
            menuSelected?.invoke(it)
        }

        binding.customToolbar.toolbar.setNavigationOnClickListener{
            listenerBack.invoke()
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
                CalendarDialog
                    .newInstance(selectedDate = now(), allowPastDates = true)
                    .listener(::selectDateMenu)
                    .show(supportFragmentManager)
                true
            }
            R.id.action_logoff -> {
                homeViewModel.logoff()
                true
            }
            R.id.action_config -> {
                startActivity<ConfigActivity>()
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

    fun selectMenuBottom(menu: BottomBar.MenuBottom) {
        binding.bottomBar.setMenuSelected(menu)
        updateTitleMenu(menu)
        menuSelected?.invoke(menu)
    }

    private fun selectDateMenu(dtSelected: Date) {
        showBackIcon(false)
        dateSelected = dtSelected
        selectMenuBottom(BottomBar.MenuBottom.AGENDA)
        dateListener?.invoke(dtSelected)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                homeViewModel.savePermissionAllowNotification(true)
            } else {
                homeViewModel.savePermissionAllowNotification(false)
                Toast.makeText(this, "Você não irá receber notificações de atividades", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestPermissionPush() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_POST_NOTIFICATIONS)
            }
        }
    }

}