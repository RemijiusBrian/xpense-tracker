package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManager
import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpensePreferencesManager
import com.ridill.xpensetracker.feature_settings.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Singleton
    @Provides
    fun provideSettingsUseCases(
        appPreferencesManager: AppPreferencesManager,
        expensePreferencesManager: ExpensePreferencesManager
    ): SettingsUseCases = SettingsUseCases(
        getAppPreference = GetAppPreferenceUseCase(appPreferencesManager),
        getExpensePreferences = GetExpensePreferencesUseCase(expensePreferencesManager),
        updateAppTheme = UpdateAppThemeUseCase(appPreferencesManager),
        updateCashFlowIncludedInExpenditure =
        UpdateCashFlowIncludedInExpenditureUseCase(expensePreferencesManager)
    )
}