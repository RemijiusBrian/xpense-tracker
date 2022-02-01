package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.ExpensePreferenceManager
import com.ridill.xpensetracker.feature_dashboard.domain.model.DashboardPreferences
import kotlinx.coroutines.flow.Flow

class GetExpensePreferenceUseCase(
    private val expenseStore: ExpensePreferenceManager
) {
    operator fun invoke(): Flow<DashboardPreferences> = expenseStore.preferences
}