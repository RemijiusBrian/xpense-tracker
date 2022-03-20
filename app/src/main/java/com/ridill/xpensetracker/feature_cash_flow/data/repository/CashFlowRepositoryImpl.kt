package com.ridill.xpensetracker.feature_cash_flow.data.repository

import com.ridill.xpensetracker.core.util.DispatcherProvider
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.domain.model.AgentWithAggregate
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CashFlowRepositoryImpl(
    private val dao: CashFlowDao,
    private val dispatcherProvider: DispatcherProvider
) : CashFlowRepository {

    override fun getAgents(searchQuery: String): Flow<List<AgentWithAggregate>> =
        dao.getAgentsWithAggregate(searchQuery)

    override fun getCashFlowForAgent(agent: Long): Flow<List<CashFlow>> =
        dao.getCashFlowForAgent(agent).map { entities ->
            entities.map { it.toCashFlow() }
        }

    override fun getTotalCashFlowAmount(): Flow<Long> =
        dao.getTotalCashFlowAmount()

    override fun getAggregateAmountOfAgent(agent: Long): Flow<Long> =
        dao.getAggregateAmountOfAgent(agent)

    override suspend fun getAgentById(id: Long): CashFlowAgent? =
        withContext(dispatcherProvider.io) {
            dao.getAgentById(id)?.toAgent()
        }

    override suspend fun getAgentByName(name: String): CashFlowAgent? =
        withContext(dispatcherProvider.io) {
            dao.getAgentByName(name)?.toAgent()
        }

    override suspend fun getCashFlowById(id: Long): CashFlow? =
        withContext(dispatcherProvider.io) {
            dao.getCashFlowById(id)?.toCashFlow()
        }

    override suspend fun cacheAgent(agent: CashFlowAgent): Long =
        withContext(dispatcherProvider.io) {
            dao.insertAgent(agent.toEntity())
        }

    override suspend fun cacheCashFlow(cashFlow: CashFlow): Long =
        dao.insertCashFlow(cashFlow.toEntity())

    override suspend fun clearAgentWithCashFlow(agent: CashFlowAgent) =
        withContext(dispatcherProvider.io) {
            dao.clearAgent(agent.toEntity())
        }

    override suspend fun deleteCashFlow(cashFlow: CashFlow): Int =
        withContext(dispatcherProvider.io) {
            dao.deleteCashFlow(cashFlow.toEntity())
        }
}


