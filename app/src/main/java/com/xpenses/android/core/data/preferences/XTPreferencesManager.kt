package com.xpenses.android.core.data.preferences

import com.xpenses.android.core.domain.model.XTPreferences
import kotlinx.coroutines.flow.Flow

interface XTPreferencesManager {

    val preferences: Flow<XTPreferences>

    suspend fun updateExpenditureLimit(limit: Long)
}