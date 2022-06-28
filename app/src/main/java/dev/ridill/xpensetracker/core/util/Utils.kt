package dev.ridill.xpensetracker.core.util

val <T> T.exhaustive: T
    get() = this

inline fun <T> tryOrNull(tryBlock: () -> T): T? = try {
    tryBlock()
} catch (t: Throwable) {
    t.printStackTrace()
    null
}

fun Double?.orZero(): Double = this ?: 0.0

fun Float?.orZero(): Float = this ?: 0f

fun String.toDoubleOrZero(): Double = this.toDoubleOrNull().orZero()

fun String.toLongOrZero(): Long = this.toLongOrNull() ?: 0L

object DatePatterns {
    const val LONG_DAY_NAME_WITH_DAY_NUMBER = "EEEE dd"
    const val SHORT_DAY_NAME_WITH_DAY_NUMBER = "EEEE dd"
    const val SHORT_MONTH_NAME_WITH_YEAR = "MMM-yy"
    const val MONTH_NUMBER_WITH_YEAR = "MM-yyyy"
}