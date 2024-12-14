package com.fsacchi.schoolmate.core.components

import android.os.Bundle
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.DateMasks.extensiveFormat
import com.fsacchi.schoolmate.core.extensions.format
import com.fsacchi.schoolmate.core.extensions.getDay
import com.fsacchi.schoolmate.core.extensions.getMonth
import com.fsacchi.schoolmate.core.extensions.getYear
import com.fsacchi.schoolmate.core.extensions.now
import com.fsacchi.schoolmate.core.extensions.setupFullScreen
import com.fsacchi.schoolmate.core.platform.BaseDialog
import com.fsacchi.schoolmate.databinding.DialogCalendarBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class CalendarDialog : BaseDialog<DialogCalendarBinding>() {

    private val calendar: Calendar = Calendar.getInstance()
    private var listener: ((Date) -> Unit)? = null

    override val layoutRes: Int
        get() = R.layout.dialog_calendar

    override fun init() {
        selectedDate()?.let {
//            if (it > 0) {
//                val calendarDay = Calendar.getInstance().apply {
//                    timeInMillis = it
//                }
//                binding.calendarView.scrollToDate(LocalDate.of(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay()))
//                calendar[calendarDay.getYear(), calendarDay.getMonth() - 1] = calendarDay.getDay()
//            } else {
//                val calendarDay = Calendar.getInstance().apply {
//                    timeInMillis = now().time
//                }
//
//                binding.calendarView.scrollToDate(LocalDate.of(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay()))
//                calendar[calendarDay.getYear(), calendarDay.getMonth() - 1] = calendarDay.getDay()
//            }
        }

        binding.tvTitle.text = now().format(extensiveFormat)
            .replace("-feira", "").capitalize()

    }

    override fun onResume() {
        super.onResume()
        setupFullScreen {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun created() {
        insertListeners()
    }

    fun listener(listener: (Date) -> Unit) = apply {
        this.listener = listener
    }

    private fun insertListeners() {
        binding.btnApply.setOnClickListener {
            listener?.invoke(calendar.time)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun minDate() = arguments?.getLong(MIN_DATE)
    private fun selectedDate() = arguments?.getLong(SELECTED_DATE)

    companion object {
        private const val MIN_DATE = "min_date"
        private const val SELECTED_DATE = "selected_date"

        fun newInstance(minDate: Date? = now(), selectedDate: Date? = null) =
            CalendarDialog().apply {
                arguments = Bundle().apply {
                    putLong(MIN_DATE, minDate?.time ?: -1)
                    putLong(SELECTED_DATE, selectedDate?.time ?: 0)
                }
            }
    }
}
