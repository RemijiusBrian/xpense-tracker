package dev.ridill.xpensetracker.core.domain.model

import dev.ridill.xpensetracker.core.util.Constants

data class XTPreferences(
    val expenditureLimit: Long,
    val theme: AppTheme,
    val useDynamicTheming: Boolean,
    val balanceWarningPercent: Float
) {
    val balanceWarningEnabled: Boolean
        get() = balanceWarningPercent > Constants.ZERO_F

    val isExpenditureLimitSet: Boolean
        get() = expenditureLimit > 0
}