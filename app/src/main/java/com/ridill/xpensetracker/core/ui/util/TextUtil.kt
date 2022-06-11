package com.ridill.xpensetracker.core.ui.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object TextUtil {
    private val dateFormat = SimpleDateFormat.getDateInstance()
    private val numberFormat = NumberFormat.getNumberInstance()

    fun formatNumber(number: Long): String =
        numberFormat.format(number)

    fun formatDate(dateInMillis: Long, pattern: Int = SimpleDateFormat.MEDIUM): String =
        SimpleDateFormat.getDateInstance(pattern).format(Date(dateInMillis))

    fun formatDateWithPattern(dateInMillis: Long, pattern: String): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(dateInMillis)

    fun formatAmountWithCurrency(amount: Long): String =
        "$currencySymbol ${formatNumber(amount)}"

    val currencySymbol: String
        get() = Currency.getInstance(Locale.getDefault()).symbol
}