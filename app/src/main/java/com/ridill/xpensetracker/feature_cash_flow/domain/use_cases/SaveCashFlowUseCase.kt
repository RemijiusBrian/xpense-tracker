package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository

class SaveCashFlowUseCase(
    private val repository: CashFlowRepository
) {
    suspend operator fun invoke(cashFlow: CashFlow) {
        repository.cacheCashFlow(cashFlow)
    }
}