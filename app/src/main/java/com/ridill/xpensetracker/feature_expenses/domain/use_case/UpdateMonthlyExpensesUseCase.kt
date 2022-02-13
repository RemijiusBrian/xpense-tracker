package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository

class UpdateMonthlyExpensesUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke() {
        val monthlyExpenses = repository.getMonthlyExpenses()
        if (monthlyExpenses.isNotEmpty()) {
            monthlyExpenses.forEach { expense ->
                repository.cacheExpense(expense.copy(dateMillis = System.currentTimeMillis()))
            }
        }

    }
}