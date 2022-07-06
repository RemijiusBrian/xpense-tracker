package dev.ridill.xpensetracker.core.domain.model

import androidx.annotation.StringRes
import dev.ridill.xpensetracker.R

enum class AppTheme(
    @StringRes val label: Int
) {
    SYSTEM_DEFAULT(R.string.theme_system_default),
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark)
}