package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.core.util.SimpleResponse
import kotlinx.coroutines.flow.first

class UpdateExpenditureLimitUseCase(
    private val preferenceManager: XTPreferencesManager
) {
    suspend operator fun invoke(limit: String): SimpleResponse {
        val amount = limit.toLongOrNull() ?: preferenceManager.preferences.first().expenditureLimit
        if (amount < 0) {
            return Response.Error(R.string.error_amount_invalid)
        }
        preferenceManager.updateExpenditureLimit(amount)
        return Response.Success(Unit)
    }
}