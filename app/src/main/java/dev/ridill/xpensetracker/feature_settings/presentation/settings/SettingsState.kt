package dev.ridill.xpensetracker.feature_settings.presentation.settings

import dev.ridill.xpensetracker.core.domain.model.AppTheme

data class SettingsState(
    val appTheme: AppTheme = AppTheme.SYSTEM_DEFAULT,
    val useDynamicTheme: Boolean = false,
    val expenditureLimit: String = "",
    val showThemeSelection: Boolean = false,
    val showExpenditureUpdate: Boolean = false
) {
    companion object {
        val INITIAL = SettingsState()
    }
}