package com.ridill.xpensetracker.core.util

import androidx.annotation.StringRes

typealias SimpleResponse = Response<Unit>

sealed class Response<T>(
    val data: T? = null,
    @StringRes val message: Int? = null
) {
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(@StringRes message: Int, data: T? = null) : Response<T>(data, message)
}