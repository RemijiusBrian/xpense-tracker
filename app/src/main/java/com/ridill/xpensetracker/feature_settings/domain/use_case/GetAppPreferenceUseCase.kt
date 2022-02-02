package com.ridill.xpensetracker.feature_settings.domain.use_case

import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManager
import com.ridill.xpensetracker.core.domain.model.AppPreferences
import kotlinx.coroutines.flow.Flow

class GetAppPreferenceUseCase(
    private val preferencesManager: AppPreferencesManager
) {
    operator fun invoke(): Flow<AppPreferences> = preferencesManager.preferences
}