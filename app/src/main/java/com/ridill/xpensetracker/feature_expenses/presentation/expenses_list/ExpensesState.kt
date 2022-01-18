package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import com.ridill.xpensetracker.core.domain.model.SortOrder
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.domain.model.SortCriteria

data class ExpensesState(
    val expenses: List<Expense> = emptyList(),
    val selectedExpenseCategory: ExpenseCategory? = null,
    val expenditureLimit: String = "",
    val currentExpenditure: String = "",
    val balance: String = "",
    val balancePercentage: Float = 0f,
    val isBalanceEmpty: Boolean = false,
    val selectedSortCriteria: SortCriteria = SortCriteria.BY_DATE,
    val selectedSortOrder: SortOrder = SortOrder.ASCENDING,
    val showAllExpenses: Boolean = false,
    val showExpenditureLimitUpdateDialog: Boolean = false,
) {
    companion object {
        val initial: ExpensesState = ExpensesState()
    }
}