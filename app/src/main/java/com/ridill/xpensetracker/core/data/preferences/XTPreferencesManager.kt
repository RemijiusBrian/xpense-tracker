package com.ridill.xpensetracker.core.data.preferences

import com.ridill.xpensetracker.core.domain.model.AppPreferences
import com.ridill.xpensetracker.core.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface XTPreferencesManager {

    val preferences: Flow<AppPreferences>

    suspend fun updateAppTheme(theme: AppTheme)

    suspend fun updateExpenditureLimit(limit: Long)

    suspend fun updateCashFlowIncludedInExpenditure(include: Boolean)

    suspend fun toggleIsFirstLaunchFalse()
}