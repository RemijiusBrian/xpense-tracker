package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.feature_dashboard.data.preferences.ExpensePreferenceManager
import com.ridill.xpensetracker.feature_dashboard.domain.use_case.*
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.ridill.xpensetracker.feature_expenses.domain.use_case.DeleteExpenseUseCase
import com.ridill.xpensetracker.feature_expenses.domain.use_case.SaveExpenseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    // Dashboard UseCases
    @Singleton
    @Provides
    fun provideDashboardUseCases(
        repository: ExpenseRepository,
        dashboardStore: ExpensePreferenceManager
    ): DashboardUseCases = DashboardUseCases(
        getExpenses = GetExpensesUseCase(repository),
        getExpenditureForCurrentMonth = GetExpenditureForCurrentMonthUseCase(repository),
        getExpensePreference = GetExpensePreferenceUseCase(dashboardStore),
        updateExpenditureLimit = UpdateExpenditureLimitUseCase(dashboardStore),
        deleteExpense = DeleteExpenseUseCase(repository),
        saveExpense = SaveExpenseUseCase(repository),
        updatePreferenceCategory = UpdatePreferenceCategoryUseCase(dashboardStore),
        getBalance = GetBalanceUseCase(repository, dashboardStore),
        updateShowPreviousEntries = UpdateShowPreviousEntriesUseCase(dashboardStore)
    )
}