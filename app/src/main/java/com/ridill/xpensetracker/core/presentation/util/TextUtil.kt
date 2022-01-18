package com.ridill.xpensetracker.core.presentation.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object TextUtil {
    fun formatNumber(number: Int): String =
        NumberFormat.getNumberInstance().format(number)

    fun formatNumber(number: Long): String =
        NumberFormat.getNumberInstance().format(number)

    fun formatDate(dateInMillis: Long): String =
        SimpleDateFormat.getDateInstance().format(Date(dateInMillis))

    val currencySymbol: String
        get() = Currency.getInstance(Locale.getDefault()).symbol
}