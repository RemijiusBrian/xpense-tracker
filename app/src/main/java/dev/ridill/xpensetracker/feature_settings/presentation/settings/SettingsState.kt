package dev.ridill.xpensetracker.feature_settings.presentation.settings

import dev.ridill.xpensetracker.core.domain.model.AppTheme

data class SettingsState(
    val appTheme: AppTheme = AppTheme.SYSTEM_DEFAULT,
    val expenditureLimit: String = "",
    val showThemeSelection: Boolean = false,
    val showExpenditureUpdate: Boolean = false,
    val balanceWarningPercent: Float = 0f,
    val showBalanceWarningPercentPicker: Boolean = false
) {
    companion object {
        val INITIAL = SettingsState()
    }
}