package com.fsacchi.schoolmate.core.features.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.BottomBar
import com.fsacchi.schoolmate.core.components.CalendarDialog
import com.fsacchi.schoolmate.core.extensions.DateMasks
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.format
import com.fsacchi.schoolmate.core.extensions.isToday
import com.fsacchi.schoolmate.core.extensions.navTo
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.core.extensions.showMessage
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.extensions.visible
import com.fsacchi.schoolmate.core.features.home.adapter.DisciplineListAdapter
import com.fsacchi.schoolmate.core.features.home.adapter.JobListAdapter
import com.fsacchi.schoolmate.core.features.home.sheets.JobBottomSheet
import com.fsacchi.schoolmate.core.features.home.sheets.OptionsBottomSheet
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.features.splash.SplashActivity
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.home.JobHomeModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.databinding.FragmentHomeBinding
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.features.JobViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.JobHomeUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import com.fsacchi.schoolmate.validator.extension.text
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Date

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var homeActivity: HomeActivity
    private val homeViewModel: HomeViewModel by inject()
    private var dateSelected: Date = now()
    private lateinit var jobSelected: JobModel
    private val dialog by lazy { createProgressDialog() }
    private val adapterJob by lazy { JobListAdapter() }
    private val adapterJobNextWeek by lazy { JobListAdapter() }
    private var jobHomeModel: JobHomeModel = JobHomeModel()
    private val jobViewModel: JobViewModel by inject()

    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun start() {
        homeActivity = (activity as HomeActivity)
        binding.showEmptyState = true
        binding.item = jobHomeModel
        bindAdapter(jobHomeModel)
        homeViewModel.getUser()
        observe()
        insertListeners()

        homeActivity.dateListener {
            homeViewModel.getJobs(homeActivity.user.uid, it)
        }
    }


    private fun observe() {
        lifecycleScope.launch {
            homeViewModel.uiState.user.collect { userUiState ->
                userUiState?.let {
                    when(it.screenType) {
                        UserUiState.ScreenType.Await -> {}
                        is UserUiState.ScreenType.Loaded -> {
                            homeActivity.setLoggedUser(it.screenType.userEntity)
                            refreshJobs()
                        }
                    }
                }
            }
        }


        lifecycleScope.launch {
            homeViewModel.uiState.homeJob.collect { jobUiState ->
                jobUiState?.let {
                    when(it.screenType) {
                        JobHomeUiState.ScreenType.Await -> {
                            dialog.show()
                        }
                        is JobHomeUiState.ScreenType.Error -> {
                            dialog.dismiss()
                            homeActivity.showAlertMessage(
                                isError = true,
                                title = it.screenType.errorTitle ?: "Erro ao carregar atividades",
                                message = it.screenType.errorMessage.orEmpty()
                            )
                        }
                        is JobHomeUiState.ScreenType.Loaded -> {
                            dialog.dismiss()
                            jobHomeModel = it.screenType.jobHomeModel
                            binding.item = jobHomeModel
                            bindAdapter(jobHomeModel)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            jobViewModel.uiState.deleteJob.collect { deleteUiState ->
                deleteUiState?.let {
                    when(it.screenType) {
                        is DefaultUiState.ScreenType.Error -> {
                            dialog.dismiss()
                            homeActivity.showAlertMessage(
                                isError = true,
                                title = it.screenType.errorTitle ?: "Erro ao deletar atividade",
                                message = it.screenType.errorMessage.orEmpty()
                            )
                        }
                        DefaultUiState.ScreenType.Loading -> {
                            dialog.show()
                        }
                        DefaultUiState.ScreenType.Success -> {
                            homeViewModel.getJobs(homeActivity.user.uid, jobHomeModel.dateSelected)
                        }
                    }
                }
            }
        }
    }

    private fun bindAdapter(jobHomeModel: JobHomeModel) {
        binding.showEmptyState = jobHomeModel.jobsToday.isEmpty() && jobHomeModel.nextJobs.isEmpty()
        binding.existJobsInDate = jobHomeModel.jobsToday.isNotEmpty()
        binding.showNextWeek = jobHomeModel.dateSelected.isToday() && jobHomeModel.nextJobs.isNotEmpty()

        binding.clToday.visible(jobHomeModel.dateSelected.isToday())

        binding.rvJobsDaySelected.adapter = adapterJob.apply {
            submitList(jobHomeModel.jobsToday)
            rootListener = {
                jobSelected = it
                showJobBottomSheet(it)
            }
            listenerOptions = {
                jobSelected = it
                showOptionsMenu()
            }
            listenerFinish = {
                jobSelected = it
                jobViewModel.updateJobModel(it, homeActivity.user.uid)
            }
        }

        binding.rvJobsNextDays.adapter = adapterJobNextWeek.apply {
            submitList(jobHomeModel.nextJobs)
            rootListener = {
                jobSelected = it
                showJobBottomSheet(it)
            }
            listenerOptions = {
                jobSelected = it
                showOptionsMenu()
            }
            listenerFinish = {
                jobSelected = it
                jobViewModel.updateJobModel(it, homeActivity.user.uid)
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

        binding.btnCreateJob.clickListener {
            showJobBottomSheet(JobModel())
        }

        binding.clDate.clickListener {
            CalendarDialog
                .newInstance(selectedDate = jobHomeModel.dateSelected, allowPastDates = true)
                .listener(::setDtDelivery)
                .show(childFragmentManager)
        }
    }

    private fun setDtDelivery(it: Date) {
        homeViewModel.getJobs(homeActivity.user.uid, it)
    }

    private fun showOptionsMenu() {
        OptionsBottomSheet
            .newInstance(jobMenuItems)
            .setListener(::handleOptionsMenu)
            .show(childFragmentManager)
    }

    private fun handleOptionsMenu(optionItem: OptionItem) {
        when(optionItem.desc) {
            R.string.edit -> {
                showJobBottomSheet(jobSelected)
            }
            R.string.delete -> {
                showMessage {
                    title(R.string.warning)
                    message(getString(R.string.confirm_delete_job, jobSelected.titleJob()))
                    positiveListener {
                        jobViewModel.deleteJobModel(jobSelected, homeActivity.user.uid)
                    }
                    negativeListener {}
                }
            }
        }
    }

    private fun showJobBottomSheet(jobModel: JobModel) {
        JobBottomSheet.newInstance(
            null,
            jobModel = jobModel,
            homeActivity.user.uid,
            successListener = {
                homeViewModel.getJobs(homeActivity.user.uid, jobHomeModel.dateSelected)
            },
            errorListener = { errorMessage ->
                homeActivity.showAlertMessage(
                    isError = true,
                    title = "Erro ao cadastrar atividade",
                    message = errorMessage

                )
            },
            newDisciplineListener = {
                homeActivity.selectMenuBottom(BottomBar.MenuBottom.DISCIPLINE)
            }
        ).show(childFragmentManager)
    }

    private fun refreshJobs() {
        if (homeActivity.dateSelected != null) {
            homeActivity.dateSelected?.let{ date ->
                dateSelected = date
            }
        }
        homeActivity.dateSelected = null
        homeViewModel.getJobs(homeActivity.user.uid, dateSelected)
    }
}
