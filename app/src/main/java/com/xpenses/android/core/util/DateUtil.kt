package com.xpenses.android.core.util

import java.util.*

object DateUtil {

    fun isBothMillisOfSameMonth(
        firstDateMillis: Long,
        secondDateMillis: Long
    ): Boolean {
        val firstCalendar = Calendar.getInstance().also { it.timeInMillis = firstDateMillis }
        val secondCalendar = Calendar.getInstance().also { it.timeInMillis = secondDateMillis }

        return firstCalendar.month == secondCalendar.month
    }

    fun getMonthFromMillis(dateMillis: Long): Int =
        Calendar.getInstance().apply { timeInMillis = dateMillis }.get(Calendar.MONTH)

    fun getDayFromMillis(dateMillis: Long): Int =
        Calendar.getInstance().apply { timeInMillis = dateMillis }.get(Calendar.DAY_OF_MONTH)

    fun getCurrentMonth(): Int = Calendar.getInstance().month

    fun getCurrentDay(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    private val Calendar.month
        get() = get(Calendar.MONTH)
}