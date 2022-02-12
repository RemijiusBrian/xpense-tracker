package com.ridill.xpensetracker.core.domain.use_case

import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.core.domain.model.AppPreferences
import kotlinx.coroutines.flow.Flow

class GetPreferencesUseCase(
    private val preferencesManager: XTPreferencesManager
) {
    operator fun invoke(): Flow<AppPreferences> = preferencesManager.preferences
}