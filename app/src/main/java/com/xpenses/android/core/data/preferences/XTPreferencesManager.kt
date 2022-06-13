package com.xpenses.android.core.data.preferences

import com.xpenses.android.core.domain.model.AppPreferences
import kotlinx.coroutines.flow.Flow

interface XTPreferencesManager {

    val preferences: Flow<AppPreferences>

    suspend fun updateExpenditureLimit(limit: Long)
}