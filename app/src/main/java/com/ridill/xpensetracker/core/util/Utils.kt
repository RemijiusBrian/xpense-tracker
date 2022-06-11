package com.ridill.xpensetracker.core.util

val <T> T.exhaustive: T
    get() = this

inline fun <T> tryOrNull(tryBlock: () -> T): T? = try {
    tryBlock()
} catch (t: Throwable) {
    t.printStackTrace()
    null
}

fun String.toDoubleOrZero(): Double = this.toDoubleOrNull() ?: 0.0

fun String.toLongOrZero(): Long = this.toLongOrNull() ?: 0L