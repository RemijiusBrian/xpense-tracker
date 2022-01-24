package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository

class SaveCashFlowUseCase(
    private val repository: CashFlowRepository
) {
    suspend operator fun invoke(cashFlow: CashFlow): Response<Long> {
        if (cashFlow.name.isEmpty()) return Response.Error(R.string.error_name_empty)
        if (cashFlow.amount <= 0L) return Response.Error(R.string.error_amount_invalid)

        return Response.Success(repository.cacheCashFlow(cashFlow))
    }
}