package com.xpenses.android.feature_expenses.presentation.expenses_list

import com.xpenses.android.feature_expenses.domain.model.ExpenseListItem
import com.xpenses.android.feature_expenses.domain.model.MonthAndExpenditure

data class ExpensesState(
    val tags: List<String> = emptyList(),
    val selectedTag: String = "",
    val monthsToExpenditurePercents: List<MonthAndExpenditure> = emptyList(),
    val selectedMonth: String = "",
    val expenses: List<ExpenseListItem> = emptyList(),
    val expenditureLimit: Long = 0L,
    val tagDeletableModeActive: Boolean = false,
    val showExpenditureLimitUpdateDialog: Boolean = false,
    val showTagDeleteConfirmation: Boolean = false
) {
    companion object {
        val INITIAL: ExpensesState = ExpensesState()
    }
}