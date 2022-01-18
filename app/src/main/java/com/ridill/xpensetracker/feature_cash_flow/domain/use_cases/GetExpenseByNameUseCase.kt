package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository

class GetExpenseByNameUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(name: String): Expense? =
        expenseRepository.getExpenseByName(name)
}