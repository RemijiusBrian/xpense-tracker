package com.ridill.xpensetracker.feature_cash_flow.data.repository

import com.ridill.xpensetracker.core.util.DispatcherProvider
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowAgentDao
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.domain.model.AgentWithAggregate
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CashFlowRepositoryImpl(
    private val agentDao: CashFlowAgentDao,
    private val cashFlowDao: CashFlowDao,
    private val dispatcherProvider: DispatcherProvider
) : CashFlowRepository {

    override fun getAgents(searchQuery: String): Flow<List<AgentWithAggregate>> =
        agentDao.getAgentsWithCashFlow(searchQuery).map { agentWithCashFlow ->
            agentWithCashFlow.map { it.toAgentWithAggregate() }
        }

    override fun getCashFlowForAgent(agent: Long): Flow<List<CashFlow>> =
        cashFlowDao.getCashFlowForAgent(agent).map { entities ->
            entities.map { it.toCashFlow() }
        }

    override fun getTotalCashFlowAmount(): Flow<Long> =
        cashFlowDao.getTotalCashFlowAmount()

    override fun getAggregateAmountOfAgent(agent: Long): Flow<Long> =
        cashFlowDao.getAggregateAmountOfAgent(agent)

    override suspend fun getAgentById(id: Long): CashFlowAgent? =
        withContext(dispatcherProvider.io) {
            agentDao.getAgentById(id)?.toAgent()
        }

    override suspend fun getAgentByName(name: String): CashFlowAgent? =
        withContext(dispatcherProvider.io) {
            agentDao.getAgentByName(name)?.toAgent()
        }

    override suspend fun getCashFlowById(id: Long): CashFlow? =
        withContext(dispatcherProvider.io) {
            cashFlowDao.getCashFlowById(id)?.toCashFlow()
        }

    override suspend fun cacheAgent(agent: CashFlowAgent): Long =
        withContext(dispatcherProvider.io) {
            agentDao.insertAgent(agent.toEntity())
        }

    override suspend fun cacheCashFlow(cashFlow: CashFlow): Long =
        withContext(dispatcherProvider.io) {
            cashFlowDao.insertCashFlow(cashFlow.toEntity())
        }

    override suspend fun clearAgentWithCashFlow(agent: CashFlowAgent): Int =
        withContext(dispatcherProvider.io) {
            cashFlowDao.deleteAllCashFlowForAgent(agent.id)
            agentDao.deleteAgent(agent.toEntity())
        }

    override suspend fun deleteCashFlow(cashFlow: CashFlow): Int =
        withContext(dispatcherProvider.io) {
            cashFlowDao.deleteCashFlow(cashFlow.toEntity())
        }
}


