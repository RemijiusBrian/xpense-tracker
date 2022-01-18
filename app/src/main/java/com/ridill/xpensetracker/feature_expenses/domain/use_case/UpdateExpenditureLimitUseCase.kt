package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.core.util.SimpleResponse
import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpenseStore
import kotlinx.coroutines.flow.first

class UpdateExpenditureLimitUseCase(
    private val expenseStore: ExpenseStore
) {
    suspend operator fun invoke(limit: String): SimpleResponse {
        val amount = limit.toLongOrNull() ?: expenseStore.preferences.first().expenditureLimit
        if (amount <= 0) {
            return Response.Error(R.string.error_amount_invalid)
        }
        expenseStore.updateExpenditureLimit(amount)
        return Response.Success(Unit)
    }
}