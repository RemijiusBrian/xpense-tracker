package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import kotlinx.coroutines.flow.Flow

class GetTotalCashFlowAmountUseCase(
    private val repository: CashFlowRepository
) {
    operator fun invoke(): Flow<Long> = repository.getTotalCashFlow()
}