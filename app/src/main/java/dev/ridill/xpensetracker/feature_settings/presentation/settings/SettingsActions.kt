package dev.ridill.xpensetracker.feature_settings.presentation.settings

import dev.ridill.xpensetracker.core.domain.model.AppTheme

interface SettingsActions {
    fun onThemePreferenceClick()
    fun onAppThemeSelectionDismiss()
    fun onAppThemeSelectionConfirm(theme: AppTheme)
    fun onUseDynamicCheckedChange(isChecked: Boolean)
    fun onExpenditureLimitPreferenceClick()
    fun onExpenditureLimitUpdateDismiss()
    fun onExpenditureLimitUpdateConfirm(amount: String)
}