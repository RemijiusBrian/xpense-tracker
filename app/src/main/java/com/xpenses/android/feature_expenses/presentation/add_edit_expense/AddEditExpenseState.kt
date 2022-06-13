package com.xpenses.android.feature_expenses.presentation.add_edit_expense

import com.xpenses.android.feature_expenses.domain.model.Expense

data class AddEditExpenseState(
    val expense: Expense? = null,
    val tagsList: List<String> = emptyList(),
    val newTagModeActive: Boolean = false,
    val showDeleteConfirmation: Boolean = false
) {
    companion object {
        val INITIAL = AddEditExpenseState()
    }
}