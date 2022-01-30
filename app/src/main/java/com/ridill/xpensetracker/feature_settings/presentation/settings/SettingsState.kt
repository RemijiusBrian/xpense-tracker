package com.ridill.xpensetracker.feature_settings.presentation.settings

import com.ridill.xpensetracker.core.domain.model.AppTheme

data class SettingsState(
    val showAppThemeDialog: Boolean = false,
    val currentAppTheme: AppTheme? = null
) {
    companion object {
        val INITIAL = SettingsState()
    }
}