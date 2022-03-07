package com.ridill.xpensetracker.feature_expenses.presentation.expenses

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense

interface ExpensesActions {
    fun onExpenseClick(expense: Expense)
    fun onAddExpenseClick()
    fun onExpenseSwipeDeleted(expense: Expense)
    fun onDateSelect(date: String)
    fun onEditExpenditureLimitClick()
    fun onExpenditureLimitUpdateDismiss()
    fun onExpenditureLimitUpdateConfirm(limit: String)
}