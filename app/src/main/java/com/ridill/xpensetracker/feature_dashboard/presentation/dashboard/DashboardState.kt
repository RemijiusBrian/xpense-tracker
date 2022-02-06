package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard

import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory

data class DashboardState(
    val expenditures: Map<String, List<Expenditure>> = emptyMap(),
    val selectedExpenditureCategory: ExpenditureCategory? = null,
    val expenditureLimit: String = "",
    val currentExpenditure: String = "",
    val balance: String = "",
    val balancePercentage: Float = 0f,
    val isBalanceEmpty: Boolean = false,
    val showExpenditureLimitUpdateDialog: Boolean = false,
    val currentlyShownMonth: String = ""
) {
    companion object {
        val INITIAL: DashboardState = DashboardState()
    }
}