package com.ridill.xpensetracker.feature_expenses.data.preferences

import com.ridill.xpensetracker.feature_expenses.domain.model.ExpensePreferences
import kotlinx.coroutines.flow.Flow

interface ExpensePreferencesManager {

    val preferences: Flow<ExpensePreferences>

    suspend fun updateExpenditureLimit(limit: Long)
}