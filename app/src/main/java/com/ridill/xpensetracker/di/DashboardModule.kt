package com.ridill.xpensetracker.di

import android.app.Application
import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManager
import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManagerImpl
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

    // Dashboard Preferences Manager
    @Singleton
    @Provides
    fun provideDashboardPreferencesManager(application: Application): DashboardPreferencesManager =
        DashboardPreferencesManagerImpl(application)

    // Dashboard UseCases
    @Singleton
    @Provides
    fun provideDashboardUseCases(
        repository: ExpenseRepository,
        dashboardStore: DashboardPreferencesManager
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