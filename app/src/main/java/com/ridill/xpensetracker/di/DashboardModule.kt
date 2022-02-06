package com.ridill.xpensetracker.di

import android.app.Application
import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManager
import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManagerImpl
import com.ridill.xpensetracker.feature_dashboard.domain.use_case.*
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.DeleteExpenditureUseCase
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.SaveExpenditureUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    // Dashboard Preferences
    @Singleton
    @Provides
    fun provideDashboardPreferences(application: Application): DashboardPreferencesManager =
        DashboardPreferencesManagerImpl(application)

    // Dashboard UseCases
    @Singleton
    @Provides
    fun provideDashboardUseCases(
        repository: ExpenditureRepository,
        dashboardStore: DashboardPreferencesManager
    ): DashboardUseCases = DashboardUseCases(
        getExpenditures = GetExpendituresUseCase(repository),
        getExpenditureForCurrentMonth = GetExpenditureForCurrentMonthUseCase(repository),
        getDashboardPreference = GetDashboardPreferenceUseCase(dashboardStore),
        updateExpenditureLimit = UpdateExpenditureLimitUseCase(dashboardStore),
        deleteExpenditure = DeleteExpenditureUseCase(repository),
        saveExpenditure = SaveExpenditureUseCase(repository),
        updatePreferenceCategory = UpdatePreferenceCategoryUseCase(dashboardStore),
        getBalance = GetBalanceUseCase(repository, dashboardStore),
    )
}