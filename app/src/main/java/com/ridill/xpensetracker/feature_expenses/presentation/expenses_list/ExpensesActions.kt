package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list

interface ExpensesActions {
    fun onExpenditureLimitUpdateClick()
    fun onExpenditureLimitUpdateDialogDismiss()
    fun onExpenditureLimitUpdateDialogConfirm(limit: String)
    fun onTagFilterSelect(tag: String)
    fun onTagLongClick()
    fun onTagDeleteClick(tag: String)
    fun onDeleteExpensesWithTagDismiss()
    fun onDeleteExpensesWithTagConfirm()
    fun onAddFabClick()
    fun onMonthSelect(month: String)
    fun onExpenseClick(id: Long)
}