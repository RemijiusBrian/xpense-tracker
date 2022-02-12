package com.ridill.xpensetracker.feature_settings.domain.use_case

import com.ridill.xpensetracker.core.domain.use_case.GetPreferencesUseCase

data class SettingsUseCases(
    val getPreferences: GetPreferencesUseCase,
    val updateAppTheme: UpdateAppThemeUseCase,
    val updateCashFlowIncludedInExpenditure: UpdateCashFlowIncludedInExpenditureUseCase
)