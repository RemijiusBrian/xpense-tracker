package com.xpenses.android.core.util

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