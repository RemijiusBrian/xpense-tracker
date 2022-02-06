package com.ridill.xpensetracker.feature_settings.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.domain.use_case.GetDashboardPreferenceUseCase
import com.ridill.xpensetracker.feature_dashboard.domain.use_case.UpdateExpenditureLimitUseCase

data class SettingsUseCases(
    val getDashboardPreferences: GetDashboardPreferenceUseCase,
    val updateExpenditureLimitUseCase: UpdateExpenditureLimitUseCase,
    val getAppPreference: GetAppPreferenceUseCase,
    val updateAppTheme: UpdateAppThemeUseCase
)
