package com.ridill.xpensetracker.feature_settings.domain.use_case

import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.core.domain.model.AppTheme

class UpdateAppThemeUseCase(
    private val preferencesManager: XTPreferencesManager
) {
    suspend operator fun invoke(theme: AppTheme) {
        preferencesManager.updateAppTheme(theme)
    }
}