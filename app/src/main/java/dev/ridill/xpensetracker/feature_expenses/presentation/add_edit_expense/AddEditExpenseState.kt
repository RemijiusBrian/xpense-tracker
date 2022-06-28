package dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

data class AddEditExpenseState(
    val tagsList: List<String> = emptyList(),
    val selectedTag: String? = null,
    val tagInputExpanded: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val isBillExpense: Boolean = true
) {
    companion object {
        val INITIAL = AddEditExpenseState()
    }
}