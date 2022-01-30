package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManager
import com.ridill.xpensetracker.feature_dashboard.domain.model.DashboardPreferences
import kotlinx.coroutines.flow.Flow

class GetExpensePreferenceUseCase(
    private val expenseStore: DashboardPreferencesManager
) {
    operator fun invoke(): Flow<DashboardPreferences> = expenseStore.preferences
}