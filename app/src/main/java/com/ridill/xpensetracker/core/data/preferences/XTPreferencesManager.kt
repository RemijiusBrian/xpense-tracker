package com.ridill.xpensetracker.core.data.preferences

import com.ridill.xpensetracker.core.domain.model.AppPreferences
import kotlinx.coroutines.flow.Flow

interface XTPreferencesManager {

    val preferences: Flow<AppPreferences>

    suspend fun updateExpenditureLimit(limit: Long)

    suspend fun updateCashFlowIncludedInExpenditure(include: Boolean)
}