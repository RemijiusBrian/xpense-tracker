package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpenseStore
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

class UpdatePreferenceCategoryUseCase(
    private val expenseStore: ExpenseStore
) {
    suspend operator fun invoke(category: ExpenseCategory) {
        expenseStore.updateExpenseCategory(category)
    }
}