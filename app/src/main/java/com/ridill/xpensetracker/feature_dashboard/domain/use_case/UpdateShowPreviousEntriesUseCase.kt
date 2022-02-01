package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.ExpensePreferenceManager

class UpdateShowPreviousEntriesUseCase(
    private val expenseStore: ExpensePreferenceManager
) {
    suspend operator fun invoke(show: Boolean) {
        expenseStore.updateShowPreviousEntries(show)
    }
}