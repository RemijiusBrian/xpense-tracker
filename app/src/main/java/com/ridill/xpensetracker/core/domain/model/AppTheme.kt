package com.ridill.xpensetracker.core.domain.model

import androidx.annotation.StringRes
import com.ridill.xpensetracker.R

enum class AppTheme(@StringRes val label: Int) {
    SYSTEM_DEFAULT(R.string.system_default),
    LIGHT(R.string.light),
    DARK(R.string.dark)
}