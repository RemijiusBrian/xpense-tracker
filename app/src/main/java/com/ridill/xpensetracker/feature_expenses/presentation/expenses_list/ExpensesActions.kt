package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseMenuOption

interface ExpensesActions {
    fun onExpenseClick(expense: Expense)
    fun addExpenseClick(category: ExpenseCategory)
    fun onExpenditureLimitCardClick()
    fun dismissExpenditureLimitUpdateDialog()
    fun updateExpenditureLimit(limit: String)
    fun onExpenseSwipeDeleted(expense: Expense)
    fun onExpenseCategorySelect(category: ExpenseCategory)
    fun onMenuOptionClick(option: ExpenseMenuOption)
}