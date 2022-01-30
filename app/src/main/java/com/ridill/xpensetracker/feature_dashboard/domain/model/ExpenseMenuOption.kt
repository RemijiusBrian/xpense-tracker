package com.ridill.xpensetracker.feature_dashboard.domain.model

sealed class ExpenseMenuOption {
    data class ShowAllEntries(val show: Boolean) : ExpenseMenuOption()
}