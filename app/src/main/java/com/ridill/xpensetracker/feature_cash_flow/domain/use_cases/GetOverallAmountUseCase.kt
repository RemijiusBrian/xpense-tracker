package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetOverallAmountUseCase(
    private val repository: CashFlowRepository
) {
    operator fun invoke(expenseId: Long): Flow<Long> {
        return repository.getCashFlowForExpense(expenseId).map { cashFlow ->
            val (lent, borrowed) = cashFlow.partition { it.lending }
            val lentAmount = lent.takeIf { it.isNotEmpty() }?.sumOf { it.amount } ?: 0L
            val borrowedAmount = borrowed.takeIf { it.isNotEmpty() }?.sumOf { it.amount } ?: 0L

            lentAmount - borrowedAmount
        }
    }
}