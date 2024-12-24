package com.fsacchi.schoolmate.core.components

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class NonSelectedDayDecorator(
    private val colors: List<Int>,
    private val specificDay: CalendarDay
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == specificDay
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(MultiDotSpan(colors))
    }
}

class MultiDotSpan(private val colors: List<Int>) : LineBackgroundSpan {
    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        charSequence: CharSequence,
        start: Int,
        end: Int,
        lineNum: Int
    ) {
        val totalDots = colors.size
        val dotRadius = 5f
        val centerX = (left + right) / 2f
        val centerY = bottom + dotRadius * 3

        val originalColor = paint.color
        val spacing = 15f

        for ((index, color) in colors.withIndex()) {
            val cx = centerX - (spacing * (totalDots - 1) / 2f) + (index * spacing)
            paint.color = color
            canvas.drawCircle(cx, centerY, dotRadius, paint)
        }
        paint.color = originalColor
    }
}