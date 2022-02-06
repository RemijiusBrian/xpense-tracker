package com.ridill.xpensetracker.feature_dashboard.domain.model

import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

data class DashboardPreferences(
    val expenditureLimit: Long,
    val category: ExpenseCategory,
)