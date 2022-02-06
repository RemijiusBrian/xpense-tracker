package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_expenditures.domain.use_case.DeleteExpenditureUseCase
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.SaveExpenditureUseCase

data class DashboardUseCases(
    val getExpenditures: GetExpendituresUseCase,
    val getExpenditureForCurrentMonth: GetExpenditureForCurrentMonthUseCase,
    val getBalance: GetBalanceUseCase,
    val updatePreferenceCategory: UpdatePreferenceCategoryUseCase,
    val getDashboardPreference: GetDashboardPreferenceUseCase,
    val updateExpenditureLimit: UpdateExpenditureLimitUseCase,
    val deleteExpenditure: DeleteExpenditureUseCase,
    val saveExpenditure: SaveExpenditureUseCase,
)