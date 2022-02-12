package com.ridill.xpensetracker.feature_cash_flow.data.repository

import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CashFlowRepositoryImpl(
    private val dao: CashFlowDao,
) : CashFlowRepository {

    override fun getAgents(searchQuery: String): Flow<List<CashFlowAgent>> =
        dao.getAgents(searchQuery).map { entities -> entities.map { it.toAgent() } }

    override fun getCashFlowForAgent(agent: Long): Flow<List<CashFlow>> =
        dao.getCashFlowForAgent(agent).map { entities ->
            entities.map { it.toCashFlow() }
        }

    override fun getTotalCashFlowAmount(): Flow<Long> =
        dao.getTotalCashFlowAmount()

    override suspend fun getAgentById(id: Long): CashFlowAgent? =
        dao.getAgentById(id)?.toAgent()

    override suspend fun getAgentByName(name: String): CashFlowAgent? =
        dao.getAgentByName(name)?.toAgent()

    override suspend fun getCashFlowById(id: Long): CashFlow? =
        dao.getCashFlowById(id)?.toCashFlow()

    override suspend fun cacheAgent(agent: CashFlowAgent): Long =
        dao.insertAgent(agent.toEntity())

    override suspend fun cacheCashFlow(cashFlow: CashFlow): Long =
        dao.insertCashFlow(cashFlow.toEntity())

    override suspend fun clearCashFlowWithAgent(agent: CashFlowAgent) {
        dao.clearAgent(agent.toEntity())
    }

    override suspend fun deleteCashFlow(cashFlow: CashFlow) {
        dao.deleteCashFlow(cashFlow.toEntity())
    }
}

