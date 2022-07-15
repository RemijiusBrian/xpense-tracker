package dev.ridill.xpensetracker.core.domain.model

data class XTPreferences(
    val expenditureLimit: Long,
    val theme: AppTheme,
    val useDynamicTheming: Boolean,
    val showWarningUnderBalancePercent: Float
)