package com.xpenses.android.core.util

import java.util.*

fun Long.getMonthFromMillis(): Int =
    Calendar.getInstance().apply { timeInMillis = this@getMonthFromMillis }.month

fun Long.getDayFromMillis(): Int =
    Calendar.getInstance().apply { timeInMillis = this@getDayFromMillis }.dayOfMonth

fun getCurrentMonth(): Int = Calendar.getInstance().month

fun getCurrentDay(): Int = Calendar.getInstance().dayOfMonth

val Calendar.year
    get() = get(Calendar.YEAR)

val Calendar.month
    get() = get(Calendar.MONTH)

val Calendar.dayOfMonth
    get() = get(Calendar.DAY_OF_MONTH)