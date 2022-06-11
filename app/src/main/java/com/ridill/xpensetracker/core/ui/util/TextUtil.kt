package com.ridill.xpensetracker.core.ui.util

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

    fun formatAmountWithCurrency(amount: Double): String =
        "$currencySymbol ${formatNumber(amount)}"

    fun formatAmountWithCurrency(amount: Long): String =
        "$currencySymbol ${formatNumber(amount)}"

    val currencySymbol: String
        get() = Currency.getInstance(Locale.getDefault()).symbol
}