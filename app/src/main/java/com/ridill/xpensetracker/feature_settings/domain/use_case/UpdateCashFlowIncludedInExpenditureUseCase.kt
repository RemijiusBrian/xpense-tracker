package com.ridill.xpensetracker.feature_settings.domain.use_case

import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManager

class UpdateCashFlowIncludedInExpenditureUseCase(
    private val preferencesManager: AppPreferencesManager
) {
    suspend operator fun invoke(include: Boolean) {
        preferencesManager.updateCashFlowInlcudedInExpenditure(include)
    }
}