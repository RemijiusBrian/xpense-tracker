package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.AggregateAmountState

class MapToCashFlowStatusUseCase {

    operator fun invoke(cashFlow: List<CashFlow>, aggregate: Long): AggregateAmountState? {
        return when {
            cashFlow.isEmpty() -> AggregateAmountState.CLEARED
            cashFlow.isNotEmpty() && aggregate == 0L -> AggregateAmountState.BALANCED
            aggregate > 0 -> AggregateAmountState.IN_CREDIT
            aggregate < 0 -> AggregateAmountState.IN_DEBT
            else -> null
        }
    }
}