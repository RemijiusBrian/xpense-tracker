package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensesUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(
        category: ExpenseCategory,
        showAll: Boolean,
    ): Flow<List<Expense>> = repository.getExpenses(category, showAll)
}
