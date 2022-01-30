package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManager

class UpdateShowPreviousEntriesUseCase(
    private val expenseStore: DashboardPreferencesManager
) {
    suspend operator fun invoke(show: Boolean) {
        expenseStore.updateShowPreviousEntries(show)
    }
}