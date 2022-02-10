package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository

class SaveOrGetExistingAgentUseCase(
    private val repository: CashFlowRepository
) {
    suspend operator fun invoke(
        tryExisting: Boolean,
        agent: CashFlowAgent
    ): Response<CashFlowAgent> {
        if (agent.name.isEmpty()) return Response.Error(R.string.error_name_empty)

        if (tryExisting) {
            val cashFlowAgent = repository.getAgentByName(agent.name)
            if (cashFlowAgent != null) {
                return Response.Success(cashFlowAgent)
            }
        }

        val insertedId = repository.cacheAgent(agent.copy(name = agent.name.trim()))
        return repository.getAgentById(insertedId)?.let {
            Response.Success(it)
        } ?: Response.Error(R.string.error_unknown)
    }
}
