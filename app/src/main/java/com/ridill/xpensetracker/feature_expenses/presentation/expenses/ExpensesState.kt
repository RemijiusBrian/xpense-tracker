package com.ridill.xpensetracker.feature_expenses.presentation.expenses

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense

data class ExpensesState(
    val monthsList: List<String> = emptyList(),
    val expenses: List<Expense> = emptyList(),
    val expenditureLimit: String = "",
    val currentExpenditure: String = "",
    val spendingBalance: String = "",
    val balancePercentage: Float = 0f,
    val selectedDate: String = "",
    val showExpenditureLimitUpdateDialog: Boolean = false,
) {
    companion object {
        val INITIAL: ExpensesState = ExpensesState()
    }
}