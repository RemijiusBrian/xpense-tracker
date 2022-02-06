package com.ridill.xpensetracker.feature_expenditures.presentation.add_edit_expenditure

interface AddEditExpenditureActions {
    fun onNameChange(value: String)
    fun onAmountChange(value: String)
    fun onSaveClick()
    fun onRepeatEveryMonthToggle(repeat: Boolean)
    fun onDeleteOptionClick()
    fun onDeleteDialogDismissed()
    fun onDeleteDialogConfirmed()
}