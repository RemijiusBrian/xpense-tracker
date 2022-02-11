package com.ridill.xpensetracker.feature_settings.domain.use_case

data class SettingsUseCases(
    val getAppPreference: GetAppPreferenceUseCase,
    val getExpensePreferences: GetExpensePreferencesUseCase,
    val updateAppTheme: UpdateAppThemeUseCase,
    val updateCashFlowIncludedInExpenditure: UpdateCashFlowIncludedInExpenditureUseCase
)