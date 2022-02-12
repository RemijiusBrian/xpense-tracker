package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.core.domain.use_case.GetPreferencesUseCase
import com.ridill.xpensetracker.feature_settings.domain.use_case.SettingsUseCases
import com.ridill.xpensetracker.feature_settings.domain.use_case.UpdateAppThemeUseCase
import com.ridill.xpensetracker.feature_settings.domain.use_case.UpdateCashFlowIncludedInExpenditureUseCase
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
        preferencesManager: XTPreferencesManager,
    ): SettingsUseCases = SettingsUseCases(
        getPreferences = GetPreferencesUseCase(preferencesManager),
        updateAppTheme = UpdateAppThemeUseCase(preferencesManager),
        updateCashFlowIncludedInExpenditure =
        UpdateCashFlowIncludedInExpenditureUseCase(preferencesManager)
    )
}