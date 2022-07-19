package dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import dev.ridill.xpensetracker.feature_expenses.domain.model.ExpenseListItem
import dev.ridill.xpensetracker.feature_expenses.domain.model.MonthStats

data class ExpensesState(
    val tags: List<String> = emptyList(),
    val selectedTag: String = "",
    val monthsToExpenditurePercents: List<MonthStats> = emptyList(),
    val selectedMonth: String = "",
    val expenses: List<ExpenseListItem> = emptyList(),
    val balance: Double = 0.0,
    val balancePercent: Float = 0f,
    val showLowBalanceWarning: Boolean = false,
    val tagDeletionModeActive: Boolean = false,
    val showTagDeleteConfirmation: Boolean = false,
    val isLimitSet: Boolean = false,
    val multiSelectionModeActive: Boolean = false,
    val selectedExpenseIds: List<Long> = emptyList(),
    val showDeleteExpensesConfirmation: Boolean = false
) {
    companion object {
        val INITIAL: ExpensesState = ExpensesState()
    }
}