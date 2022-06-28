package com.xpenses.android.feature_bills.presentation.add_edit_bill

import com.xpenses.android.feature_bills.domain.model.BillCategory

interface AddEditBillActions {
    fun onNameChange(value: String)
    fun onAmountChange(value: String)
    fun onMarkAsRecurringCheckChange(isChecked: Boolean)
    fun onCategoryClick()
    fun onCategorySelectionDismiss()
    fun onCategorySelect(category: BillCategory)
    fun onPayByDateChange(dateMillis: Long)
    fun onSave()
    fun onDeleteClick()
    fun onDeleteDismiss()
    fun onDeleteConfirm()
}