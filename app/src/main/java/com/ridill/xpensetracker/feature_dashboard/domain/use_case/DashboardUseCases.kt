package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_expenses.domain.use_case.DeleteExpenseUseCase
import com.ridill.xpensetracker.feature_expenses.domain.use_case.SaveExpenseUseCase

data class DashboardUseCases(
    val getExpenses: GetExpensesUseCase,
    val getExpenditureForCurrentMonth: GetExpenditureForCurrentMonthUseCase,
    val getBalance: GetBalanceUseCase,
    val updateExpenseCategoryPreference: UpdateExpenseCategoryPreferenceUseCase,
    val getDashboardPreference: GetDashboardPreferenceUseCase,
    val deleteExpense: DeleteExpenseUseCase,
    val saveExpense: SaveExpenseUseCase,
    val updateExpenditureLimit: UpdateExpenditureLimitUseCase
)