package com.fsacchi.schoolmate.core.features.home

import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.showMessage
import com.fsacchi.schoolmate.core.features.home.adapter.JobListAdapter
import com.fsacchi.schoolmate.core.features.home.sheets.JobBottomSheet
import com.fsacchi.schoolmate.core.features.home.sheets.OptionsBottomSheet
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.databinding.FragmentDisciplineJobsBinding
import com.fsacchi.schoolmate.presentation.features.JobViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.JobUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DisciplineJobsFragment : BaseFragment<FragmentDisciplineJobsBinding>() {

    private lateinit var disciplineSelected: DisciplineModel
    private lateinit var jobSelected: JobModel
    private lateinit var homeActivity: HomeActivity
    private val dialog by lazy { createProgressDialog() }
    private val jobViewModel: JobViewModel by inject()
    private val adapter by lazy { JobListAdapter() }

    override val layoutRes: Int
        get() = R.layout.fragment_discipline_jobs

    override fun start() {
        homeActivity = (activity as HomeActivity)
        binding.showEmptyState = true
        insertListeners()
        observe()
    }

    override fun onResume() {
        super.onResume()
        jobViewModel.getJobs(homeActivity.user.uid, disciplineSelected, true)
    }

    private fun observe() {
        lifecycleScope.launch {
            jobViewModel.uiState.jobs.collect { jobUiState ->
                jobUiState?.let {
                    when(it.screenType) {
                        JobUiState.ScreenType.Await -> {
                            dialog.show()
                        }
                        is JobUiState.ScreenType.Error -> {
                            dialog.dismiss()
                            homeActivity.showAlertMessage(
                                isError = true,
                                title = it.screenType.errorTitle ?: "Erro ao carregar atividades",
                                message = it.screenType.errorMessage.orEmpty()
                            )
                        }
                        is JobUiState.ScreenType.Loaded -> {
                            dialog.dismiss()
                            bindAdapter(it.screenType.listJob)
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
                            jobViewModel.getJobs(homeActivity.user.uid, disciplineSelected, true)
                        }
                    }
                }
            }
        }
    }

    fun setDisciplineSelected(disciplineModel: DisciplineModel) {
        disciplineSelected = disciplineModel
    }

    private fun bindAdapter(listJob: List<JobModel>) {
        binding.showEmptyState = listJob.isEmpty()

        binding.rvJob.adapter = adapter.apply {
            submitList(listJob)
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

    private fun insertListeners() {
        binding.btnCreateJob.clickListener {
            showJobBottomSheet(JobModel())
        }
    }

    private fun showJobBottomSheet(jobModel: JobModel) {
        JobBottomSheet.newInstance(
            disciplineSelected,
            jobModel = jobModel,
            homeActivity.user.uid,
            successListener = {
                jobViewModel.getJobs(homeActivity.user.uid, disciplineSelected, true)
            },
            errorListener = { errorMessage ->
                homeActivity.showAlertMessage(
                    isError = true,
                    title = "Erro ao cadastrar atividade",
                    message = errorMessage

                )
            }
        ).show(childFragmentManager)
    }
}
