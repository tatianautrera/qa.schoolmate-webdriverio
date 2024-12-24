package com.fsacchi.schoolmate.presentation.features

import android.content.SharedPreferences
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.core.extensions.toBase64
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.local.extensions.PreferencesKeys
import com.fsacchi.schoolmate.data.local.extensions.put
import com.fsacchi.schoolmate.data.model.home.JobCalendarModel
import com.fsacchi.schoolmate.data.model.login.ConfigModel
import com.fsacchi.schoolmate.data.repository.UserRepository
import com.fsacchi.schoolmate.domain.home.GetUserUseCase
import com.fsacchi.schoolmate.domain.home.LogoffUseCase
import com.fsacchi.schoolmate.domain.job.GetJobHomeUseCase
import com.fsacchi.schoolmate.domain.job.GetJobsCalendarUseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.JobCalendarUiState
import com.fsacchi.schoolmate.presentation.states.JobHomeUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val logoffUseCase: LogoffUseCase,
    private val sharedPreferences: SharedPreferences,
    private val getJobHomeUseCase: GetJobHomeUseCase,
    private val getJobsCalendarUseCase: GetJobsCalendarUseCase
) : ViewModel(), LifecycleObserver {

    val uiState = UiState()

    fun logoff() {
        viewModelScope.launch{
            logoffUseCase().collect {
                uiState.logoff.emit(it)
            }
        }
    }

    fun savePermissionAllowNotification(allowPostNotification: Boolean) {
        sharedPreferences.apply {
            put(PreferencesKeys.ALLOW_RECEIVE_NOTIFICATIONS.name, allowPostNotification)
        }
    }

    fun getConfigNotification(): ConfigModel {
        sharedPreferences.apply {
            return ConfigModel(
                allowNotification = getBoolean(PreferencesKeys.ALLOW_RECEIVE_NOTIFICATIONS.name, false),
                daysUntilHomeWork = getString(PreferencesKeys.DAYS_UNTIL_HOMEWORK.name, "3").orEmpty(),
                daysUntilTest = getString(PreferencesKeys.DAYS_UNTIL_TEST.name, "3").orEmpty(),
                daysUntilJob = getString(PreferencesKeys.DAYS_UNTIL_JOB.name, "3").orEmpty()
            )
        }
    }

    fun saveConfigNotification(configModel: ConfigModel, allowNotification: Boolean) {
        sharedPreferences.apply {
            put(PreferencesKeys.ALLOW_RECEIVE_NOTIFICATIONS.name, allowNotification)
            put(PreferencesKeys.DAYS_UNTIL_HOMEWORK.name,
                configModel.daysUntilHomeWork.ifEmpty { 1 })
            put(PreferencesKeys.DAYS_UNTIL_JOB.name,
                configModel.daysUntilJob.ifEmpty { 1 })
            put(PreferencesKeys.DAYS_UNTIL_TEST.name,
                configModel.daysUntilTest.ifEmpty { 1 })
        }
    }

    fun getUser() {
        viewModelScope.launch {
            getUserUseCase().collect {
                uiState.user.emit(it)
            }
        }
    }

    fun getJobs(userId: String, dateSelected: Date) {
        viewModelScope.launch {
            val params = GetJobHomeUseCase.Params(userId, dateSelected)
            getJobHomeUseCase(params).collect {
                uiState.homeJob.emit(it)
            }
        }
    }

    fun getCalendarJobs(userId: String) {
        viewModelScope.launch {
            val params = GetJobsCalendarUseCase.Params(userId)
            getJobsCalendarUseCase(params).collect {
                uiState.jobCalendar.emit(it)
            }
        }
    }

    data class UiState(
        val logoff: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val user: MutableSharedFlow<UserUiState?> = MutableSharedFlow(),
        val homeJob: MutableSharedFlow<JobHomeUiState?> = MutableSharedFlow(),
        val jobCalendar: MutableSharedFlow<JobCalendarUiState?> = MutableSharedFlow()
    )
}