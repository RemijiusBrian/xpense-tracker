package com.xpenses.android.feature_bills.presentation.add_edit_bill

import com.xpenses.android.feature_bills.domain.model.BillCategory

data class AddEditBillState(
    val showCategorySelection: Boolean = false,
    val showDeletionConfirmation: Boolean = false,
    val payByDate: String = "",
    val category: BillCategory = BillCategory.MISC,
    val isBillRecurring: Boolean = false
) {
    companion object {
        val INITIAL = AddEditBillState()
    }
}