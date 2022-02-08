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
    fun provideDashboardPreferenceManager(application: Application): DashboardPreferencesManager =
        DashboardPreferencesManagerImpl(application)

    // Dashboard UseCases
    @Singleton
    @Provides
    fun provideDashboardUseCases(
        repository: ExpenseRepository,
        dashboardPreferencesManager: DashboardPreferencesManager
    ): DashboardUseCases = DashboardUseCases(
        getMonthsList = GetMonthListUseCase(repository),
        getExpensesForMonth = GetExpensesForMonthUseCase(repository),
        getExpenditureForCurrentMonth = GetExpenditureForCurrentMonthUseCase(repository),
        getDashboardPreference = GetDashboardPreferenceUseCase(dashboardPreferencesManager),
        deleteExpense = DeleteExpenseUseCase(repository),
        saveExpense = SaveExpenseUseCase(repository),
        updateExpenseCategoryPreference = UpdateExpenseCategoryPreferenceUseCase(dashboardPreferencesManager),
        getBalance = GetBalanceUseCase(repository, dashboardPreferencesManager),
        updateExpenditureLimit = UpdateExpenditureLimitUseCase(dashboardPreferencesManager)
    )
}