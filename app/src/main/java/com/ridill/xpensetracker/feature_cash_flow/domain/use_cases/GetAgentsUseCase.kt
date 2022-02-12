package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import kotlinx.coroutines.flow.Flow

class GetAgentsUseCase(
    private val repository: CashFlowRepository
) {
    operator fun invoke(query: String): Flow<List<CashFlowAgent>> = repository.getAgents(query)
}