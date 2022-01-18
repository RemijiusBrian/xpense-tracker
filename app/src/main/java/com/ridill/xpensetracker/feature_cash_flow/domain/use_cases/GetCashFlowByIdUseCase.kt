package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository

class GetCashFlowByIdUseCase(
    private val repository: CashFlowRepository
) {
    suspend operator fun invoke(expenseId: Long, name: String): CashFlow? =
        repository.getCashFlowById(expenseId, name)
}