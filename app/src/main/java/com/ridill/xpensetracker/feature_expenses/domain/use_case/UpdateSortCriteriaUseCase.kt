package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpenseStore
import com.ridill.xpensetracker.feature_expenses.domain.model.SortCriteria

class UpdateSortCriteriaUseCase(
    private val expenseStore: ExpenseStore
) {
    suspend operator fun invoke(criteria: SortCriteria) {
        expenseStore.updateSortCriteria(criteria)
    }
}