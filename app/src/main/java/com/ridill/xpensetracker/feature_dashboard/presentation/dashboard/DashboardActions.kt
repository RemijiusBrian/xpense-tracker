package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

interface DashboardActions {
    fun onExpenseClick(expense: Expense)
    fun addExpenseClick(category: ExpenseCategory)
    fun onExpenditureLimitUpdate()
    fun dismissExpenditureLimitUpdateDialog()
    fun updateExpenditureLimit(limit: String)
    fun onExpenseSwipeDeleted(expense: Expense)
    fun onExpenseCategorySelect(category: ExpenseCategory)
    fun onSettingsClick()
    fun onMonthClick(month: String)
}