package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository

class SaveExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Response<Long> {
        val trimmedExpense = expense.copy(name = expense.name.trim())
        if (trimmedExpense.name.isEmpty()) {
            return Response.Error(message = R.string.error_name_empty)
        }
        if (expense.amount < 0) {
            return Response.Error(message = R.string.error_amount_invalid)
        }
        return Response.Success(repository.cacheExpense(trimmedExpense))
    }
}