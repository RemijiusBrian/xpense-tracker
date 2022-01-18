package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository

class DoesExpenseAlreadyExistUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(name: String): Boolean =
        expenseRepository.getExpenseByName(name) != null
}