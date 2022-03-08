package com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

interface AddEditExpenseActions {
    fun onNameChange(value: String)
    fun onAmountChange(value: String)
    fun onSaveClick()
    fun onRepeatEveryMonthToggle(repeat: Boolean)
    fun onDeleteOptionClick()
    fun onDeleteDialogDismissed()
    fun onDeleteDialogConfirmed()
    fun onTagSelect(tag: String)
    fun onNewTagClick()
    fun onNewTagConfirm(tag: String)
    fun onNewTagDismiss()
}