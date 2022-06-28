package dev.ridill.xpensetracker.feature_bills.presentation.add_edit_bill

import dev.ridill.xpensetracker.feature_bills.domain.model.BillCategory

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