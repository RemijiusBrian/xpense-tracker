package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository

class ClearCashFlowUseCase(
    private val cashFlowRepository: CashFlowRepository,
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        cashFlowRepository.clearCache()
        expenseRepository.deleteExpense(expense)
    }
}