package com.xpenses.android.core.ui.util

import com.xpenses.android.core.util.tryOrNull
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object TextUtil {
    private val numberFormat = NumberFormat.getNumberInstance()

    fun formatNumber(number: Double): String =
        numberFormat.format(number)

    private fun formatNumber(number: Long): String =
        numberFormat.format(number)

    fun formatDateWithPattern(dateInMillis: Long, pattern: String): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(dateInMillis)

    fun formatDateWithStyle(dateInMillis: Long, style: Int): String =
        SimpleDateFormat.getDateInstance(style).format(dateInMillis)

    fun formatAmountWithCurrency(amount: Double): String =
        "$currencySymbol ${formatNumber(amount)}"

    fun formatAmountWithCurrency(amount: Long): String =
        "$currencySymbol ${formatNumber(amount)}"

    fun parseDate(date: String, pattern: String): Date? = tryOrNull {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(date)
    }

    val currencySymbol: String
        get() = Currency.getInstance(Locale.getDefault()).symbol
}