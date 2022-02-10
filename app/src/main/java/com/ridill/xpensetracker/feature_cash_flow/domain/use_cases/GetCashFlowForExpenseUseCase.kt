package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import kotlinx.coroutines.flow.Flow

class GetCashFlowForExpenseUseCase(
    private val repository: CashFlowRepository
) {
    operator fun invoke(expenseId: Long): Flow<List<CashFlow>> =
        repository.getCashFlowForAgent(expenseId)
}