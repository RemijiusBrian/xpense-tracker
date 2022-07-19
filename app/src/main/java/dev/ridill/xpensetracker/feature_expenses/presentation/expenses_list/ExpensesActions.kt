package dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list

interface ExpensesActions {
    fun onTagFilterSelect(tag: String)
    fun onTagLongClick()
    fun onTagDeleteClick(tag: String)
    fun onDeleteExpensesWithTagDismiss()
    fun onDeleteExpensesWithTagConfirm()
    fun onAddFabClick()
    fun onMonthSelect(month: String)
    fun onExpenseClick(id: Long)
    fun onExpenseLongClick(id: Long)
    fun onCancelMultiSelectionMode()
    fun onDeleteOptionClick()
    fun onDeleteExpensesDismiss()
    fun onDeleteExpensesConfirm()
}