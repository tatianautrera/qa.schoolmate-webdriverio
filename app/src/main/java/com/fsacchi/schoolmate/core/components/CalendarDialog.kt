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
import com.fsacchi.schoolmate.core.extensions.toDate
import com.fsacchi.schoolmate.core.features.home.calendar.CustomSelectorDecorator
import com.fsacchi.schoolmate.core.platform.BaseDialog
import com.fsacchi.schoolmate.databinding.DialogCalendarBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class CalendarDialog : BaseDialog<DialogCalendarBinding>() {

    private val calendar: Calendar = Calendar.getInstance()
    private var listener: ((Date) -> Unit)? = null

    override val layoutRes: Int
        get() = R.layout.dialog_calendar

    override fun init() {
        val allowPastDates = arguments?.getBoolean(ALLOW_PAST_DATES)?.or(false)
        if (allowPastDates?.or(false) == true) {
            binding.tvTitle.text = getString(R.string.choose_dt_agenda)
        } else {
            binding.tvTitle.text = getString(R.string.choose_dt_delivery)
        }
        selectedDate()?.let {
            if (it > 0) {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = it
                }
                val calendarDay = CalendarDay.from(calendar.getYear(), calendar.getMonth(), calendar.getDay())

                binding.calendarView.selectedDate = calendarDay
                calendar[calendarDay.year, calendarDay.month - 1] = calendarDay.day
            } else {
                binding.calendarView.selectedDate = CalendarDay.today()
            }
        }

        if (allowPastDates?.not() == true) {
            minDate()?.let {
                val calendarDay = Calendar.getInstance().apply {
                    timeInMillis = it
                }
                binding.calendarView.state().edit()
                    .setMinimumDate(CalendarDay.from(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay()))
                    .commit()
            }
        }

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
        binding.calendarView.setOnDateChangedListener { _, calendarDay, _ ->
            calendar[calendarDay.year, calendarDay.month - 1] = calendarDay.day
        }

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
        private const val ALLOW_PAST_DATES = "allow_past_dates"

        fun newInstance(minDate: Date? = now(), selectedDate: Date? = null, allowPastDates: Boolean = false) =
            CalendarDialog().apply {
                arguments = Bundle().apply {
                    putLong(MIN_DATE, minDate?.time ?: -1)
                    putLong(SELECTED_DATE, selectedDate?.time ?: 0)
                    putBoolean(ALLOW_PAST_DATES, allowPastDates)
                }
            }
    }
}
