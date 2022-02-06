package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard

import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory

interface DashboardActions {
    fun onExpenditureClick(expenditure: Expenditure)
    fun addExpenditureClick(category: ExpenditureCategory)
    fun onExpenditureLimitUpdate()
    fun onExpenditureLimitUpdateDismiss()
    fun onExpenditureLimitUpdateConfirm(limit: String)
    fun onExpenditureSwipeDeleted(expenditure: Expenditure)
    fun onExpenditureCategorySelect(category: ExpenditureCategory)
    fun onSettingsClick()
    fun onMonthClick(month: String)
}