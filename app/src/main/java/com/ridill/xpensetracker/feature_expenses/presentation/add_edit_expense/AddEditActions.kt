package com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

interface AddEditActions {
    fun onExpenseNameChange(value: String)
    fun onAmountChange(value: String)
    fun onSaveClick()
    fun onRepeatEveryMonthToggle(repeat: Boolean)
    fun onDeleteOptionClick()
    fun onDeleteExpenseDialogDismissed()
    fun onDeleteExpenseDialogConfirmed()
}