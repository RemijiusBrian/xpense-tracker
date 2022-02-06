package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManager
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

class UpdatePreferenceCategoryUseCase(
    private val expenseStore: DashboardPreferencesManager
) {
    suspend operator fun invoke(category: ExpenseCategory) {
        expenseStore.updateExpenseCategory(category)
    }
}