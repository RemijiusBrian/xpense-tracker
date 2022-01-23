package com.ridill.xpensetracker.feature_expenses.domain.model

data class ExpensePreferences(
    val expenditureLimit: Long,
    val category: ExpenseCategory,
    val showAllEntries: Boolean
)