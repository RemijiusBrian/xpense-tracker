package com.ridill.xpensetracker.core.data.preferences

import com.ridill.xpensetracker.core.domain.model.AppPreferences
import com.ridill.xpensetracker.core.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface AppPreferencesManager {

    val preferences: Flow<AppPreferences>

    suspend fun updateAppTheme(theme: AppTheme)
}