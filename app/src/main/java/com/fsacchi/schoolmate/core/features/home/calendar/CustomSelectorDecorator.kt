package com.fsacchi.schoolmate.core.features.home.calendar

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import com.fsacchi.schoolmate.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CustomSelectorDecorator(private val context: Context, private val selectedDate: CalendarDay) : DayViewDecorator {

    private val smallerCircleDrawable = ShapeDrawable(OvalShape()).apply {
        paint.color = context.getColor(R.color.colorPrimary)
        intrinsicWidth = 2
        intrinsicHeight = 2
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == selectedDate
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(smallerCircleDrawable)
    }
}


