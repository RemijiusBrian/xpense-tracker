package com.ridill.xpensetracker.feature_dashboard.domain.model

import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory

data class DashboardPreferences(
    val expenditureLimit: Long,
    val category: ExpenditureCategory,
)