package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.core.domain.model.SortOrder
import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpenseStore

class UpdateSortOrderUseCase(
    private val expenseStore: ExpenseStore
) {
    suspend operator fun invoke(sortOrder: SortOrder) {
        expenseStore.updateSortOrder(sortOrder)
    }
}