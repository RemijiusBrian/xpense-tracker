package com.xpenses.android.feature_expenses.presentation.add_edit_expense

import com.xpenses.android.feature_expenses.domain.model.Expense

data class AddEditExpenseState(
    val expense: Expense? = null,
    val tagsList: List<String> = emptyList(),
    val tagInputExpanded: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val isBillExpense: Boolean = true
) {
    companion object {
        val INITIAL = AddEditExpenseState()
    }
}