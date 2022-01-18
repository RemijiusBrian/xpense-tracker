package com.ridill.xpensetracker.feature_expenses.domain.model

import com.ridill.xpensetracker.core.domain.model.SortOrder

sealed class ExpenseMenuOption {
    data class SortOption(val criteria: SortCriteria, val order: SortOrder) : ExpenseMenuOption()
    data class ShowAllEntries(val show: Boolean) : ExpenseMenuOption()
}