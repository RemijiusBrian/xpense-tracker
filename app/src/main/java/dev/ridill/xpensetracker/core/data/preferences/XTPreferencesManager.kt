package dev.ridill.xpensetracker.core.data.preferences

import dev.ridill.xpensetracker.core.domain.model.AppTheme
import dev.ridill.xpensetracker.core.domain.model.XTPreferences
import kotlinx.coroutines.flow.Flow

interface XTPreferencesManager {

    companion object {
        const val NAME = "XT_PREFERENCES"
    }

    val preferences: Flow<XTPreferences>

    suspend fun updateExpenditureLimit(limit: Long)
    suspend fun updateAppTheme(theme: AppTheme)
    suspend fun updateUseDynamicTheming(use: Boolean)
    suspend fun updateBalanceWarningPercent(value: Float)
}