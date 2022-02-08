package com.ridill.xpensetracker.core.ui.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object TextUtil {
    private val dateFormat = SimpleDateFormat.getDateInstance()
    private val numberFormat = NumberFormat.getNumberInstance()

    fun formatNumber(number: Int): String =
        NumberFormat.getNumberInstance().format(number)

    fun formatNumber(number: Long): String =
        NumberFormat.getNumberInstance().format(number)

    fun formatDate(dateInMillis: Long, pattern: Int = SimpleDateFormat.MEDIUM): String =
        SimpleDateFormat.getDateInstance(pattern).format(Date(dateInMillis))

    fun formatDate(date: Date): String =
        SimpleDateFormat.getDateInstance().format(date)

    val currencySymbol: String
        get() = Currency.getInstance(Locale.getDefault()).symbol
}