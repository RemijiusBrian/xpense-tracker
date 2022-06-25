package com.xpenses.android.feature_bills.presentation.add_bill

import com.xpenses.android.feature_bills.domain.model.BillCategory

interface AddBillActions {
    fun onNameChange(value: String)
    fun onAmountChange(value: String)
    fun onMarkAsRecurringCheckChange(isChecked: Boolean)
    fun onCategoryClick()
    fun onCategorySelect(category: BillCategory)
    fun onPayByDateChange(dateMillis: Long)
    fun onSave()
}