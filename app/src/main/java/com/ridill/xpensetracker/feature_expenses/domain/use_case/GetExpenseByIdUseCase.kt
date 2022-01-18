package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository

class GetExpenseByIdUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expenseId: Long): Expense? = repository.getExpenseById(expenseId)
}