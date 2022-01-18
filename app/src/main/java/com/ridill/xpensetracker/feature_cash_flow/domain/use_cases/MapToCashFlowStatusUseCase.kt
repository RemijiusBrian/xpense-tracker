package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowStatus

class MapToCashFlowStatusUseCase {

    operator fun invoke(cashFlow: List<CashFlow>, aggregate: Long): CashFlowStatus? {
        return when {
            cashFlow.isEmpty() -> CashFlowStatus.CLEARED
            cashFlow.isNotEmpty() && aggregate == 0L -> CashFlowStatus.BALANCED
            aggregate > 0 -> CashFlowStatus.IN_CREDIT
            aggregate < 0 -> CashFlowStatus.IN_DEBT
            else -> null
        }
    }
}