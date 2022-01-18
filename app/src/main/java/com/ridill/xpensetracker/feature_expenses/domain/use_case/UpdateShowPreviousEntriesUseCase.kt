package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpenseStore

class UpdateShowPreviousEntriesUseCase(
    private val expenseStore: ExpenseStore
) {
    suspend operator fun invoke(show: Boolean) {
        expenseStore.updateShowPreviousEntries(show)
    }
}