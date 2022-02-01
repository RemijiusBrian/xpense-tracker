package com.ridill.xpensetracker.feature_settings.presentation.settings

import com.ridill.xpensetracker.core.domain.model.AppTheme

data class SettingsState(
    val currentAppTheme: AppTheme? = null,
    val showAppThemeDialog: Boolean = false,
    val currentExpenditureLimit: String = "",
    val showExpenditureLimitDialog: Boolean = false
) {
    companion object {
        val INITIAL = SettingsState()
    }
}