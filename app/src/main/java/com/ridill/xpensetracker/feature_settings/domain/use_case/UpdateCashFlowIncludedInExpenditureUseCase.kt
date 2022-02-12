package com.ridill.xpensetracker.feature_settings.domain.use_case

import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager

class UpdateCashFlowIncludedInExpenditureUseCase(
    private val preferencesManager: XTPreferencesManager
) {
    suspend operator fun invoke(include: Boolean) {
        preferencesManager.updateCashFlowIncludedInExpenditure(include)
    }
}