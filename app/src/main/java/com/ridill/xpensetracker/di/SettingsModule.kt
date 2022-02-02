package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManager
import com.ridill.xpensetracker.feature_dashboard.data.preferences.ExpensePreferenceManager
import com.ridill.xpensetracker.feature_dashboard.domain.use_case.GetExpensePreferenceUseCase
import com.ridill.xpensetracker.feature_dashboard.domain.use_case.UpdateExpenditureLimitUseCase
import com.ridill.xpensetracker.feature_settings.domain.use_case.GetAppPreferenceUseCase
import com.ridill.xpensetracker.feature_settings.domain.use_case.SettingsUseCases
import com.ridill.xpensetracker.feature_settings.domain.use_case.UpdateAppThemeUseCase
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
        expensePreferenceManager: ExpensePreferenceManager,
        appPreferencesManager: AppPreferencesManager
    ): SettingsUseCases = SettingsUseCases(
        getExpensePreferences = GetExpensePreferenceUseCase(expensePreferenceManager),
        updateExpenditureLimitUseCase = UpdateExpenditureLimitUseCase(expensePreferenceManager),
        getAppPreference = GetAppPreferenceUseCase(appPreferencesManager),
        updateAppTheme = UpdateAppThemeUseCase(appPreferencesManager)
    )
}