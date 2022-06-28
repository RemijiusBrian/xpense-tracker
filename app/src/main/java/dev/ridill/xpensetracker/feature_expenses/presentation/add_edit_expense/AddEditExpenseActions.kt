package dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

interface AddEditExpenseActions {
    fun onAmountChange(value: String)
    fun onNameChange(value: String)
    fun onTagSelect(tag: String)
    fun onNewTagClick()
    fun onNewTagValueChange(value: String)
    fun onNewTagInputDismiss()
    fun onNewTagConfirm()
    fun onDeleteClick()
    fun onDeleteDismiss()
    fun onDeleteConfirm()
    fun onSave()
}