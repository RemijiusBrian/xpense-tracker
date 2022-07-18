package dev.ridill.xpensetracker.core.ui.util

import android.icu.text.CompactDecimalFormat
import dev.ridill.xpensetracker.core.util.tryOrNull
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

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
    ): String = buildString {
        if (amount < 0) append("-")
        append(currencySymbol)
        append(
            CompactDecimalFormat
                .getInstance(Locale.getDefault(), compactStyle)
                .format(abs(amount))
        )
    }

    fun formatAmountWithCurrency(amount: Double): String = buildString {
        if (amount < 0) append("-")
        append(currencySymbol)
        append(formatNumber(abs(amount)))
    }

    fun formatAmountWithCurrency(amount: Long): String = buildString {
        if (amount < 0) append("-")
        append(currencySymbol)
        append(formatNumber(abs(amount)))
    }

    fun formatPercent(percent: Float): String = buildString {
        if (percent < 0) append("-")
        append((abs(percent) * 100).roundToInt())
        append("%")
    }

    fun parseDate(date: String, pattern: String): Date? = tryOrNull {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(date)
    }

    val currencySymbol: String
        get() = Currency.getInstance(Locale.getDefault()).symbol
}