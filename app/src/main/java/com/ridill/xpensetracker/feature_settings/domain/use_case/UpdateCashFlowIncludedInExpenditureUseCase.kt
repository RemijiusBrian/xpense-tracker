package com.ridill.xpensetracker.feature_settings.domain.use_case

import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpensePreferencesManager

class UpdateCashFlowIncludedInExpenditureUseCase(
    private val preferencesManager: ExpensePreferencesManager
) {
    suspend operator fun invoke(include: Boolean) {
        preferencesManager.updateCashFlowIncludedInExpenditure(include)
    }
}