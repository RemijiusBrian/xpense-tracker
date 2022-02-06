package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManager
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory

class UpdatePreferenceCategoryUseCase(
    private val expenseStore: DashboardPreferencesManager
) {
    suspend operator fun invoke(category: ExpenditureCategory) {
        expenseStore.updateExpenseCategory(category)
    }
}