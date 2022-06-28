package com.xpenses.android.core.util

import com.xpenses.android.core.ui.util.TextUtil
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

fun Long.dayWithSuffix(
    longDay: Boolean = false,
): String = buildString {
    val pattern = if (longDay) DatePatterns.LONG_DAY_NAME_WITH_DAY_NUMBER
    else DatePatterns.SHORT_DAY_NAME_WITH_DAY_NUMBER
    val formatted = TextUtil.formatDateWithPattern(this@dayWithSuffix, pattern)
    val suffix = when (formatted.lastOrNull()) {
        null -> ""
        '1' -> "st"
        '2' -> "nd"
        '3' -> "rd"
        else -> "th"
    }
    append(formatted)
    append(suffix)
}
