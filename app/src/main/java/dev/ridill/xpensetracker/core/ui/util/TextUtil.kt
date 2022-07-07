package dev.ridill.xpensetracker.core.ui.util

import android.icu.text.CompactDecimalFormat
import dev.ridill.xpensetracker.core.util.tryOrNull
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object TextUtil {
    private val numberFormat = NumberFormat.getNumberInstance()

    private fun formatNumber(number: Double): String =
        numberFormat.format(number)

    private fun formatNumber(number: Long): String =
        numberFormat.format(number)

    fun formatDate(dateInMillis: Long, pattern: String): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(dateInMillis)

    fun formatDate(dateInMillis: Long, style: Int = SimpleDateFormat.MEDIUM): String =
        SimpleDateFormat.getDateInstance(style).format(dateInMillis)

    fun compactFormatAmountWithCurrency(
        amount: Double,
        compactStyle: CompactDecimalFormat.CompactStyle = CompactDecimalFormat.CompactStyle.SHORT
    ): String = "$currencySymbol${
        CompactDecimalFormat.getInstance(Locale.getDefault(), compactStyle).format(amount)
    }"

    fun compactFormatAmountWithCurrency(
        amount: Long,
        compactStyle: CompactDecimalFormat.CompactStyle = CompactDecimalFormat.CompactStyle.LONG
    ): String = "$currencySymbol${
        CompactDecimalFormat.getInstance(Locale.getDefault(), compactStyle).format(amount)
    }"

    fun formatAmountWithCurrency(amount: Double): String =
        "$currencySymbol${formatNumber(amount)}"

    fun formatAmountWithCurrency(amount: Long): String =
        "$currencySymbol${formatNumber(amount)}"

    fun parseDate(date: String, pattern: String): Date? = tryOrNull {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(date)
    }

    val currencySymbol: String
        get() = Currency.getInstance(Locale.getDefault()).symbol
}