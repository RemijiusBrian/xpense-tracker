package com.xpenses.android.feature_expenses.domain.model

data class ExpenseListItem(
    val id: Long,
    val name: String,
    val amount: String,
    val date: String
)