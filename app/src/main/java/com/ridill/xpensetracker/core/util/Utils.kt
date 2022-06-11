package com.ridill.xpensetracker.core.util

val <T> T.exhaustive: T
    get() = this

inline fun <T> tryOrNull(tryBlock: () -> T): T? = try {
    tryBlock()
} catch (t: Throwable) {
    t.printStackTrace()
    null
}

fun Long?.orZero(): Long = this ?: 0L