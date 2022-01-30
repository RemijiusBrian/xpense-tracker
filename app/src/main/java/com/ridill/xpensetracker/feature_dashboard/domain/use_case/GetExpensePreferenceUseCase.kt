package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardStore
import com.ridill.xpensetracker.feature_dashboard.domain.model.ExpensePreferences
import kotlinx.coroutines.flow.Flow

class GetExpensePreferenceUseCase(
    private val expenseStore: DashboardStore
) {
    operator fun invoke(): Flow<ExpensePreferences> = expenseStore.preferences
}