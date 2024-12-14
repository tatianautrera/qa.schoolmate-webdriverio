package com.fsacchi.schoolmate.core.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import java.util.Date
import java.util.TimeZone

@Throws(ParseException::class)
fun String.toDate(pattern: String = DateMasks.appFormat): Date {
    return SimpleDateFormat(pattern, locale).apply { isLenient = false }
        .parse(this)
}

fun Date.format(pattern: String): String {
    return SimpleDateFormat(pattern, locale)
        .format(this)
}

fun String.formatDateMask(
    fromPattern: String = DateMasks.appFormat,
    toPattern: String = DateMasks.serverFormat
): String {
    return try {
        this.toDate(fromPattern)?.format(toPattern).orEmpty()
    } catch (e: Throwable) {
        emptyString()
    }
}

fun Date.formatWithUTC(pattern: String): String {
    return SimpleDateFormat(pattern, locale)
        .format(utcToLocal())
}

fun Calendar.compare(calendar: Calendar?): Boolean {
    return time.compare(calendar?.time)
}

fun Calendar.getYear(): Int {
    return this.get(YEAR)
}

fun Calendar.getMonth(): Int {
    return this.get(MONTH) + 1
}

fun Calendar.getDay(): Int {
    return this.get(DAY_OF_MONTH)
}

fun Calendar.getLastDayOfMonth(month: Int, year: Int): Date {
    this.apply {
        set(MONTH, month - 1)
        set(YEAR, year)
    }
    val lastDay = this.getActualMaximum(DAY_OF_MONTH)
    this.set(DAY_OF_MONTH, lastDay)

    return this.time
}

fun Calendar.toDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(getYear(), getMonth() - 1, getDay())
    return calendar.time
}

fun Calendar.getFirstDayOfMonth(month: Int, year: Int): Date {
    this.apply {
        set(MONTH, month - 1)
        set(YEAR, year)
        set(DAY_OF_MONTH, 1)
    }
    return this.time
}

fun Date.compare(date: Date?): Boolean {
    return resetTime().time == date?.resetTime()?.time
}

fun Date.utcToLocal(): Date {
    return Date(time + TimeZone.getDefault().getOffset(time))
}

fun Date.resetTime() = apply {
    val date = this
    time = Calendar.getInstance().apply {
        time = date
    }.resetTime().timeInMillis
}

fun Date.verifyMaiority(): Boolean {
    val date = this
    val calendarDate = Calendar.getInstance().apply {
        time = date
    }.resetTime()

    val calendarLimit = Calendar.getInstance().apply {
        time = now()
    }.resetTime()

    calendarLimit.add(Calendar.YEAR, -18)
    return (calendarDate.time.before(calendarLimit.time) || calendarDate.time == calendarLimit.time)
}

fun Calendar.resetTime() = apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun addMonthFromNow(month: Int = 1): Date {
    return Calendar.getInstance().apply {
        add(MONTH, month)
    }.time
}

fun nowPlusAnyMonths(numberOfMonths: Int): Date {
    return Calendar.getInstance().apply {
        add(Calendar.MONTH, numberOfMonths)
    }.time
}

fun now(): Date {
    return Calendar.getInstance().time
}

private fun Date.add(field: Int, amount: Int): Date {
    Calendar.getInstance().apply {
        time = this@add
        add(field, amount)
        return time
    }
}

fun Date.addDays(days: Int): Date {
    return add(Calendar.DAY_OF_MONTH, days)
}

fun String.isValidDate(pattern: String = DateMasks.appFormat): Boolean {
    return try {
        toDate(pattern)
        true
    } catch (e: Throwable) {
        false
    }
}

fun String.formatDate(): String {
    return if (isNotEmpty()) {
        val result = this.split("T")
        if (result.isNotEmpty()) {
            val dates = result[0].split("-")
            if (dates.isNotEmpty() && dates.size > 2) {
                dates[2] + "/" + dates[1] + "/" + dates[0]
            } else ""
        } else ""
    } else ""
}
