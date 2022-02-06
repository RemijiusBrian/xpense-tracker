package com.ridill.xpensetracker.feature_dashboard.data.preferences

import com.ridill.xpensetracker.feature_dashboard.domain.model.DashboardPreferences
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface DashboardPreferencesManager {

    val preferences: Flow<DashboardPreferences>

    suspend fun updateExpenditureLimit(limit: Long)

    suspend fun updateExpenseCategory(category: ExpenseCategory)
}