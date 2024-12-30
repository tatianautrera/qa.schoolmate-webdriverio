package com.fsacchi.schoolmate.core.features.home.sheets

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.TextDrawable
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.extensions.getParcelable
import com.fsacchi.schoolmate.core.extensions.hideSoftKeyboard
import com.fsacchi.schoolmate.core.extensions.isEmoji
import com.fsacchi.schoolmate.core.extensions.setupFullScreen
import com.fsacchi.schoolmate.core.extensions.string
import com.fsacchi.schoolmate.core.extensions.toEmoji
import com.fsacchi.schoolmate.core.extensions.toast
import com.fsacchi.schoolmate.core.platform.BaseDialog
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.databinding.BottomSheetDisciplineBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.validator.Validator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vanniktech.emoji.EmojiPopup
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DisciplineBottomSheet : BaseDialog<BottomSheetDisciplineBinding>() {

    private lateinit var validator: Validator
    private val dialog by lazy { createProgressDialog() }

    private val disciplineViewModel: DisciplineViewModel by inject()
    private val discipline by getParcelable<DisciplineModel>(MODEL)
    private val userUid by string(USER_ID)

    private var saveAction: ((DisciplineModel) -> Unit)? = null
    private var errorAction: ((String) -> Unit)? = null
    private lateinit var emojiPopup: EmojiPopup

    override val layoutRes: Int
        get() = R.layout.bottom_sheet_discipline

    override fun init() {
        binding.item = discipline
        setEmojiInDisciplineUpdated()
        insertListeners()
    }

    private fun initEmojiPicker() {
        emojiPopup = EmojiPopup.Builder
            .fromRootView(requireView())
            .build(binding.etEmoji)

    }

    private fun setEmojiInDisciplineUpdated() {
        if (discipline.id.isNotEmpty()) {
            binding.tvTitle.text = getString(R.string.edit_discipline)

            discipline.let {
                val emojiString = it.emoji.toInt().toEmoji()
                val emojiDrawable = TextDrawable(emojiString, requireContext())
                binding.ivEmoji.setImageDrawable(emojiDrawable)
            }
        }
    }

    override fun created() {
        setupValidation()
        observe()
        initEmojiPicker()
    }

    private fun observe() {
        lifecycleScope.launch {
            disciplineViewModel.uiState.saveDiscipline.collect { disciplineUiState ->
                disciplineUiState?.let {
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
                            saveAction?.invoke(discipline)
                        }
                    }
                }
            }
        }
    }

    private fun setupValidation() {
        validator = Validator(binding)

        listOf(
            binding.tilDiscipline, binding.tilTeacher
        ).forEach {
            validator.observe(it)
        }

        validator.validateListener = {
            binding.btnSaveDiscipline.enable(it)
        }

        validator.validateNow()
    }

    private fun insertListeners() {
        binding.ivClose.clickListener(action = ::dismiss)

        binding.btnSaveDiscipline.clickListener {
            if (discipline.id.isNotEmpty()) {
                disciplineViewModel.updateDisciplineModel(discipline, userUid)
            } else {
                disciplineViewModel.saveDisciplineModel(discipline, userUid)
            }
        }

        binding.ivEmoji.clickListener {
            emojiPopup.toggle()
        }

        binding.etEmoji.doOnTextChanged { text, _, _, _ ->
            val emoji = text.toString()
            if (text?.isNotEmpty() == true) {
                val emojiPoints = emoji.codePoints().toArray().first()

                if (emoji.isNotEmpty() && emojiPoints.isEmoji()) {
                    val emojiString = emojiPoints.toEmoji()
                    val emojiDrawable = TextDrawable(emojiString, requireContext())
                    discipline.emoji = emojiPoints.toString()
                    binding.ivEmoji.setImageDrawable(emojiDrawable)
                } else {
                    toast("Emoji inválido, tente outro por favor.")
                }
                binding.etEmoji.text?.clear()
                binding.etEmoji.hideSoftKeyboard()
            }
        }
    }

    private fun setSuccessCallback(callback: ((DisciplineModel) -> Unit)?) {
        saveAction = callback
    }

    override fun onDestroyView() {
        super.onDestroyView()
        emojiPopup.dismiss()
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


        fun newInstance(
            disciplineModel: DisciplineModel,
            userUid: String,
            successListener: ((DisciplineModel) -> Unit)?,
            errorListener: ((String) -> Unit)?
        ) = DisciplineBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(MODEL, disciplineModel)
                putString(USER_ID, userUid)
            }
            setSuccessCallback(successListener)
            setErrorCallback(errorListener)
        }
    }
}
