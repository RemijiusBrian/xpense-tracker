package com.ridill.xpensetracker.feature_cash_flow.data.repository

import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CashFlowRepositoryImpl(
    private val dao: CashFlowDao,
) : CashFlowRepository {

    override fun getCashFlowForExpense(expenseId: Long): Flow<List<CashFlow>> =
        dao.getCashFlowForExpense(expenseId).map { entities ->
            entities.map { it.toCashFlow() }
        }

    override suspend fun getCashFlowById(expenseId: Long, name: String): CashFlow? =
        dao.getCashFlowById(expenseId, name)?.toCashFlow()

    override suspend fun cacheCashFlow(cashFlow: CashFlow): Long =
        dao.insert(cashFlow.toEntity())

    override suspend fun deleteCashFlow(cashFlow: CashFlow): Int =
        dao.delete(cashFlow.toEntity())

    override suspend fun clearCache() {
        dao.deleteAll()
    }
}