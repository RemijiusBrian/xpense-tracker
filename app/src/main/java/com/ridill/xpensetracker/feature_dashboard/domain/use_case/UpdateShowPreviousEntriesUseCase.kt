package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardStore

class UpdateShowPreviousEntriesUseCase(
    private val expenseStore: DashboardStore
) {
    suspend operator fun invoke(show: Boolean) {
        expenseStore.updateShowPreviousEntries(show)
    }
}