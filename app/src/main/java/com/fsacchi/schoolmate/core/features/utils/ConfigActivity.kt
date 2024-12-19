package com.fsacchi.schoolmate.core.features.utils

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
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.setOnDebouncedClickListener
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.home.HomeActivity
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.platform.BaseActivity
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.login.ConfigModel
import com.fsacchi.schoolmate.databinding.ActivityConfigBinding
import com.fsacchi.schoolmate.databinding.ActivityHomeBinding
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ConfigActivity : BaseActivity<ActivityConfigBinding>() {

    private val homeViewModel: HomeViewModel by inject()
    private val dialog by lazy { createProgressDialog() }
    private lateinit var configModel: ConfigModel

    override val layoutRes: Int get() = R.layout.activity_config

    override fun init() {
        configModel = homeViewModel.getConfigNotification()
        binding.item = configModel

        binding.customToolbar.showBackIcon(true)
        insertListeners()
    }

    override fun onBackPressed() {
        homeViewModel.saveConfigNotification(configModel, binding.swAllowNotification.isChecked)
        finish()
    }

    private fun insertListeners() {
        binding.customToolbar.toolbar.setNavigationOnClickListener{
            homeViewModel.saveConfigNotification(configModel, binding.swAllowNotification.isChecked)
            finish()
        }

        binding.swAllowNotification.setOnDebouncedClickListener {
            dialog.show()
            if (binding.swAllowNotification.isChecked) {
                configModel.allowNotification = true
                requestPermissionPush()
            } else {
                configModel.allowNotification = false
            }
            binding.item = configModel
            lifecycleScope.launch {
                delay(1000)
                dialog.dismiss()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == HomeActivity.REQUEST_CODE_POST_NOTIFICATIONS) {
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
                ActivityCompat.requestPermissions(this, arrayOf(permission),
                    HomeActivity.REQUEST_CODE_POST_NOTIFICATIONS
                )
            }
        }
    }
}