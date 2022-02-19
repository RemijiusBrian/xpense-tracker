package com.ridill.xpensetracker.feature_cash_flow.domain.repository

import com.ridill.xpensetracker.feature_cash_flow.domain.model.AgentWithAggregate
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import kotlinx.coroutines.flow.Flow

interface CashFlowRepository {

    fun getAgents(searchQuery: String): Flow<List<AgentWithAggregate>>

    fun getCashFlowForAgent(agent: Long): Flow<List<CashFlow>>

    fun getAggregateAmountOfAgent(agent: Long): Flow<Long>

    fun getTotalCashFlowAmount(): Flow<Long>

    suspend fun getAgentById(id: Long): CashFlowAgent?

    suspend fun getAgentByName(name: String): CashFlowAgent?

    suspend fun getCashFlowById(id: Long): CashFlow?

    suspend fun cacheAgent(agent: CashFlowAgent): Long

    suspend fun cacheCashFlow(cashFlow: CashFlow): Long

    suspend fun clearAgentWithCashFlow(agent: CashFlowAgent)

    suspend fun deleteCashFlow(cashFlow: CashFlow)
}