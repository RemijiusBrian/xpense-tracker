package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository

class ClearCashFlowWithAgentUseCase(
    private val repository: CashFlowRepository,
) {
    suspend operator fun invoke(agent: CashFlowAgent) {
        repository.clearCashFlowWithAgent(agent)
    }
}