package com.ridill.xpensetracker.feature_expenses.presentation.expenses

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense

interface ExpensesActions {
    fun onExpenseClick(expense: Expense)
    fun onAddExpenseClick()
    fun onExpenseSwipeDeleted(expense: Expense)
    fun onMonthSelect(month: String)
    fun onEditExpenditureLimitClick()
    fun onExpenditureLimitUpdateDialogDismissed()
    fun onExpenditureLimitUpdateDialogConfirmed(limit: String)
}