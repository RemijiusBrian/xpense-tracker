package com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

interface AddEditExpenseActions {
    fun onAmountChange(value: String)
    fun onNameChange(value: String)
    fun onMonthlyCheckChange(isChecked: Boolean)
    fun onTagSelect(tag: String)
    fun onNewTagConfirm(tag: String)
    fun onDeleteClick()
    fun onDeleteDismiss()
    fun onDeleteConfirm()
    fun onSave()
}