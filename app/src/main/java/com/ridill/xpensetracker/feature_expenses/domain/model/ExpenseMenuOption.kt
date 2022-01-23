package com.ridill.xpensetracker.feature_expenses.domain.model

sealed class ExpenseMenuOption {
    data class ShowAllEntries(val show: Boolean) : ExpenseMenuOption()
}