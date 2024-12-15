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
import com.fsacchi.schoolmate.data.model.file.FileModel
import com.fsacchi.schoolmate.data.model.file.FileUserModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.databinding.BottomSheetDisciplineBinding
import com.fsacchi.schoolmate.databinding.BottomSheetFileBinding
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

class FileBottomSheet : BaseDialog<BottomSheetFileBinding>() {

    private lateinit var validator: Validator
    private val dialog by lazy { createProgressDialog() }

    private val fileUser by getParcelable<FileUserModel>(MODEL)
    private val disciplineModel by getParcelable<DisciplineModel>(DISCIPLINE_MODEL)
    private val file by getParcelable<FileModel>(FILE_MODEL)

    private val userUid by string(USER_ID)

    private var saveAction: ((JobModel) -> Unit)? = null
    private var errorAction: ((String) -> Unit)? = null

    override val layoutRes: Int
        get() = R.layout.bottom_sheet_file

    override fun init() {
        binding.item = fileUser
        binding.file = file

        disciplineModel.let {
            if (it.name.isNotEmpty()) {
                fileUser.disciplineId = it.id
                fileUser.nameDiscipline = it.name.capitalizeFirstLetter()
                binding.tilDiscipline.isEnabled = false
            }
        }

        insertListeners()
    }

    override fun created() {
        setupValidation()
    }

    private fun setupValidation() {
        validator = Validator(binding)

        listOf(
            binding.tilDiscipline, binding.tilTitle
        ).forEach {
            validator.observe(it)
        }

        validator.validateListener = {
            binding.btnSaveFile.enable(it)
        }

        validator.validateNow()
    }

    private fun insertListeners() {
        binding.ivClose.clickListener(action = ::dismiss)
        binding.clFile.clickListener {
            //TODO: Open file
        }

        binding.btnSaveFile.clickListener {
            //TODO: Save file in collection file
        }
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
        private const val FILE_MODEL = "fileModel"

        fun newInstance(
            disciplineModel: DisciplineModel?,
            fileUserModel: FileUserModel,
            fileModel: FileModel,
            userUid: String,
            successListener: ((JobModel) -> Unit)?,
            errorListener: ((String) -> Unit)?
        ) = FileBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(MODEL, fileUserModel)
                putString(USER_ID, userUid)
                putParcelable(DISCIPLINE_MODEL, disciplineModel)
                putParcelable(FILE_MODEL, fileModel)
            }
            setSuccessCallback(successListener)
            setErrorCallback(errorListener)
        }
    }
}
