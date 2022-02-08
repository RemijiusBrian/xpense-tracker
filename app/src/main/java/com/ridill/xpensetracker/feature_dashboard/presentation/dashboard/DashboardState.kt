package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

data class DashboardState(
    val monthsList: List<String> = emptyList(),
    val expenses: List<Expense> = emptyList(),
    val selectedExpenseCategory: ExpenseCategory? = null,
    val expenditureLimit: String = "",
    val currentExpenditure: String = "",
    val balance: String = "",
    val balancePercentage: Float = 0f,
    val isBalanceEmpty: Boolean = false,
    val currentlyShownDate: String = "",
    val showExpenditureLimitUpdateDialog: Boolean = false,
) {
    companion object {
        val INITIAL: DashboardState = DashboardState()
    }
}