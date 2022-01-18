package com.ridill.xpensetracker.feature_cash_flow.domain.repository

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import kotlinx.coroutines.flow.Flow

interface CashFlowRepository {

    fun getCashFlowForExpense(expenseId: Long): Flow<List<CashFlow>>

    suspend fun getCashFlowById(expenseId: Long, name: String): CashFlow?

    suspend fun cacheCashFlow(cashFlow: CashFlow): Long

    suspend fun deleteCashFlow(cashFlow: CashFlow): Int

    suspend fun clearCache()
}