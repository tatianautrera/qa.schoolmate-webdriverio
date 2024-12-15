package com.fsacchi.schoolmate.core.features.home.sheets

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.CalendarDialog
import com.fsacchi.schoolmate.core.extensions.DateMasks.appFormat
import com.fsacchi.schoolmate.core.extensions.TextDrawable
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.extensions.format
import com.fsacchi.schoolmate.core.extensions.getParcelable
import com.fsacchi.schoolmate.core.extensions.hideSoftKeyboard
import com.fsacchi.schoolmate.core.extensions.isEmoji
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.core.extensions.setupFullScreen
import com.fsacchi.schoolmate.core.extensions.string
import com.fsacchi.schoolmate.core.extensions.toDate
import com.fsacchi.schoolmate.core.extensions.toEmoji
import com.fsacchi.schoolmate.core.extensions.toast
import com.fsacchi.schoolmate.core.platform.BaseDialog
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.databinding.BottomSheetDisciplineBinding
import com.fsacchi.schoolmate.databinding.BottomSheetJobBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import com.fsacchi.schoolmate.presentation.features.JobViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.validator.Validator
import com.fsacchi.schoolmate.validator.extension.text
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Date

class JobBottomSheet : BaseDialog<BottomSheetJobBinding>() {

    private lateinit var validator: Validator
    private val dialog by lazy { createProgressDialog() }

    private val jobViewModel: JobViewModel by inject()
    private val job by getParcelable<JobModel>(MODEL)
    private val disciplineModel by getParcelable<DisciplineModel>(DISCIPLINE_MODEL)

    private val userUid by string(USER_ID)

    private var saveAction: ((JobModel) -> Unit)? = null
    private var errorAction: ((String) -> Unit)? = null

    override val layoutRes: Int
        get() = R.layout.bottom_sheet_job

    override fun init() {
        binding.item = job
        if (job.id.isNotEmpty()) {
            if (job.status == "S") {
                binding.tvTitle.text = getString(R.string.edit_job_complete)
            } else {
                binding.tvTitle.text = getString(R.string.edit_job)
            }
        }
        disciplineModel.let {
            if (it.name.isNotEmpty()) {
                job.disciplineId = it.id
                job.nameDiscipline = it.name.capitalizeFirstLetter()
                binding.tilDiscipline.isEnabled = false
            }
        }
        insertListeners()
    }

    override fun created() {
        setupValidation()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycleScope.launch {
                jobViewModel.uiState.saveJob.collect { saveUiState ->
                    saveUiState?.let {
                        when(it.screenType) {
                            is DefaultUiState.ScreenType.Error -> {
                                dialog.dismiss()
                                errorAction?.invoke(it.screenType.errorMessage.orEmpty())
                            }
                            DefaultUiState.ScreenType.Loading -> {
                                dialog.show()
                            }
                            DefaultUiState.ScreenType.Success -> {
                                dialog.dismiss()
                                dismiss()
                                saveAction?.invoke(job)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupValidation() {
        validator = Validator(binding)

        listOf(
            binding.tilDiscipline, binding.tilJobType, binding.tilDateDelivery, binding.tilObservation
        ).forEach {
            validator.observe(it)
        }

        validator.validateListener = {
            binding.btnSaveJob.enable(it)
        }

        validator.validateNow()
    }

    private fun insertListeners() {
        binding.ivClose.clickListener(action = ::dismiss)
        binding.tilJobType.editText?.clickListener {
            if (!job.isFinish()) {
                TypeJobBottomSheet.newInstance().setListener {
                    job.typeJob = it
                    job.descrTypeJob = it.message
                    binding.item = job
                }.show(childFragmentManager)
            }
        }

        binding.etDateDelivery.clickListener {
            if (!job.isFinish()) {
                val selectedDate = if (binding.tilDateDelivery.text.isNotEmpty()) {
                    binding.tilDateDelivery.text.toDate()
                } else {
                    now()
                }
                CalendarDialog
                    .newInstance(selectedDate = selectedDate)
                    .listener(::setDtDelivery)
                    .show(childFragmentManager)
            }
        }

        binding.btnSaveJob.clickListener {
            if (job.id.isNotEmpty()) {
                jobViewModel.updateJobModel(job, userUid)
            } else {
                jobViewModel.saveJobModel(job, userUid)
            }
        }
    }

    private fun setDtDelivery(it: Date) {
        job.dtJob = it
        binding.tilDateDelivery.text = it.format(appFormat)
    }

    private fun setSuccessCallback(callback: ((JobModel) -> Unit)?) {
        saveAction = callback
    }

    private fun setErrorCallback(callback: ((String) -> Unit)?) {
        errorAction = callback
    }

    override fun onResume() {
        super.onResume()
        setupFullScreen {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    companion object {
        private const val MODEL = "model"
        private const val USER_ID = "userId"
        private const val DISCIPLINE_MODEL = "disciplineModel"

        fun newInstance(
            disciplineModel: DisciplineModel?,
            jobModel: JobModel,
            userUid: String,
            successListener: ((JobModel) -> Unit)?,
            errorListener: ((String) -> Unit)?
        ) = JobBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(MODEL, jobModel)
                putString(USER_ID, userUid)
                putParcelable(DISCIPLINE_MODEL, disciplineModel)
            }
            setSuccessCallback(successListener)
            setErrorCallback(errorListener)
        }
    }
}
