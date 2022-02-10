package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository

class DoesAgentAlreadyExistUseCase(
    private val repository: CashFlowRepository
) {
    suspend operator fun invoke(name: String): Boolean =
        repository.getAgentByName(name) != null
}