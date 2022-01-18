package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.core.domain.model.SortOrder
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.domain.model.SortCriteria
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExpensesUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(
        category: ExpenseCategory,
        sortCriteria: SortCriteria,
        sortOrder: SortOrder,
        showAll: Boolean,
    ): Flow<List<Expense>> = repository.getExpenses(category, showAll).map { expenses ->
        when (sortOrder) {
            SortOrder.ASCENDING -> {
                when (sortCriteria) {
                    SortCriteria.BY_DATE -> {
                        expenses.sortedBy { it.date }
                    }
                    SortCriteria.BY_AMOUNT -> {
                        expenses.sortedBy { it.amount }
                    }
                }
            }
            SortOrder.DESCENDING -> {
                when (sortCriteria) {
                    SortCriteria.BY_DATE -> {
                        expenses.sortedByDescending { it.date }
                    }
                    SortCriteria.BY_AMOUNT -> {
                        expenses.sortedByDescending { it.amount }
                    }
                }
            }
        }
    }
}
