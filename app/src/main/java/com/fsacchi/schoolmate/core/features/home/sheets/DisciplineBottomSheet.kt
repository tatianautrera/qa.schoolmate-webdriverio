package com.fsacchi.schoolmate.core.features.home.sheets

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.TextDrawable
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.drawable
import com.fsacchi.schoolmate.core.extensions.enable
import com.fsacchi.schoolmate.core.extensions.getParcelable
import com.fsacchi.schoolmate.core.extensions.hideSoftKeyboard
import com.fsacchi.schoolmate.core.extensions.isEmoji
import com.fsacchi.schoolmate.core.extensions.setupFullScreen
import com.fsacchi.schoolmate.core.extensions.toEmoji
import com.fsacchi.schoolmate.core.extensions.toast
import com.fsacchi.schoolmate.core.platform.BaseDialog
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.databinding.BottomSheetDisciplineBinding
import com.fsacchi.schoolmate.validator.Validator
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DisciplineBottomSheet : BaseDialog<BottomSheetDisciplineBinding>() {

    private lateinit var validator: Validator
    private val discipline by getParcelable<DisciplineModel>(MODEL)
    private var saveAction: ((DisciplineModel) -> Unit)? = null

    override val layoutRes: Int
        get() = R.layout.bottom_sheet_discipline

    override fun init() {
        binding.item = discipline
        insertListeners()
    }

    override fun created() {
        setupValidation()
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
            dismiss()
            saveAction?.invoke(discipline)
        }

        binding.ivEmoji.clickListener {
            binding.etEmoji.requestFocus()
            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etEmoji, InputMethodManager.SHOW_IMPLICIT)
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

    private fun setCallback(callback: ((DisciplineModel) -> Unit)?) {
        saveAction = callback
    }

    override fun onResume() {
        super.onResume()
        setupFullScreen {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    companion object {
        private const val MODEL = "model"

        fun newInstance(disciplineModel: DisciplineModel, listener: ((DisciplineModel) -> Unit)?) = DisciplineBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(MODEL, disciplineModel)
            }
            setCallback(listener)
        }
    }
}
