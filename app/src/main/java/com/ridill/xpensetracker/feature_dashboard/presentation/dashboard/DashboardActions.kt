package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

interface DashboardActions {
    fun onExpenseClick(expense: Expense)
    fun addExpenseFabClick(category: ExpenseCategory)
    fun onExpenseSwipeDeleted(expense: Expense)
    fun onExpenseCategorySelect(category: ExpenseCategory)
    fun onSettingsClick()
    fun onMonthClick(month: String)
    fun onEditExpenditureLimitClick()
    fun onExpenditureLimitUpdateDialogDismissed()
    fun onExpenditureLimitUpdateDialogConfirmed(limit: String)
}