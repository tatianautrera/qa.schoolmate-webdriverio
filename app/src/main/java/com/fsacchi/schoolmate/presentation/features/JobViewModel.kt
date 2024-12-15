package com.fsacchi.schoolmate.presentation.features

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.domain.discipline.DeleteDisciplineUseCase
import com.fsacchi.schoolmate.domain.discipline.GetDisciplinesUseCase
import com.fsacchi.schoolmate.domain.discipline.SaveDisciplineUseCase
import com.fsacchi.schoolmate.domain.discipline.UpdateDisciplineUseCase
import com.fsacchi.schoolmate.domain.job.DeleteJobUseCase
import com.fsacchi.schoolmate.domain.job.GetJobUseCase
import com.fsacchi.schoolmate.domain.job.SaveJobUseCase
import com.fsacchi.schoolmate.domain.job.UpdateJobUseCase
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.fsacchi.schoolmate.presentation.states.JobUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class JobViewModel(
    private val saveJobUseCase: SaveJobUseCase,
    private val getJobUseCase: GetJobUseCase,
    private val updateJobUseCase: UpdateJobUseCase,
    private val deleteJobUseCase: DeleteJobUseCase
) : ViewModel(), LifecycleObserver {

    val uiState = UiState()

    fun saveJobModel(jobModel: JobModel, userUid: String) {
        viewModelScope.launch{
            val params = SaveJobUseCase.Params(userUid, jobModel)
            saveJobUseCase(params).collect {
                uiState.saveJob.emit(it)
            }
        }
    }

    fun getJobs(userUid: String, disciplineSelected: DisciplineModel?, showOnlyJobsToDelivery: Boolean) {
        viewModelScope.launch{
            val params = GetJobUseCase.Params(userUid, disciplineSelected, showOnlyJobsToDelivery)
            getJobUseCase(params).collect {
                uiState.jobs.emit(it)
            }
        }
    }

    fun updateJobModel(jobModel: JobModel, userUid: String) {
        viewModelScope.launch{
            val params = UpdateJobUseCase.Params(userUid, jobModel)
            updateJobUseCase(params).collect {
                uiState.saveJob.emit(it)
            }
        }
    }
    fun deleteJobModel(jobModel: JobModel, userUid: String) {
        viewModelScope.launch{
            val params = DeleteJobUseCase.Params(userUid, jobModel)
            deleteJobUseCase(params).collect {
                uiState.deleteJob.emit(it)
            }
        }
    }

    data class UiState(
        val saveJob: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val deleteJob: MutableSharedFlow<DefaultUiState?> = MutableSharedFlow(),
        val jobs: MutableSharedFlow<JobUiState?> = MutableSharedFlow()
    )
}