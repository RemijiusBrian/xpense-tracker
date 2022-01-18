package com.ridill.xpensetracker.feature_expenses.domain.model

import com.ridill.xpensetracker.core.domain.model.SortOrder

data class ExpensePreferences(
    val expenditureLimit: Long,
    val sortOrder: SortOrder,
    val sortCriteria: SortCriteria,
    val category: ExpenseCategory,
    val showAllEntries: Boolean
)