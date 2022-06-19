package com.xpenses.android.core.util

import java.util.*

object DateUtil {

    fun getMonthFromMillis(dateMillis: Long): Int =
        Calendar.getInstance().apply { timeInMillis = dateMillis }.month

    fun getDayFromMillis(dateMillis: Long): Int =
        Calendar.getInstance().apply { timeInMillis = dateMillis }.dayOfMonth

    fun getCurrentMonth(): Int = Calendar.getInstance().month

    fun getCurrentDay(): Int = Calendar.getInstance().dayOfMonth

    val Calendar.year
        get() = get(Calendar.YEAR)

    val Calendar.month
        get() = get(Calendar.MONTH)

    val Calendar.dayOfMonth
        get() = get(Calendar.DAY_OF_MONTH)
}