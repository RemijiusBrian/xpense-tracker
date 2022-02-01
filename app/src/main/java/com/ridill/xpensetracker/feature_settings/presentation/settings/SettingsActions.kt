package com.ridill.xpensetracker.feature_settings.presentation.settings

import com.ridill.xpensetracker.core.domain.model.AppTheme

interface SettingsActions {
    fun onAppThemePreferenceClick()
    fun onAppThemeDialogDismiss()
    fun onAppThemeDialogConfirm(theme: AppTheme)
    fun onExpenditureLimitPreferenceClick()
    fun onExpenditureLimitDialogDismiss()
    fun onExpenditureLimitDialogConfirm(limit: String)
}