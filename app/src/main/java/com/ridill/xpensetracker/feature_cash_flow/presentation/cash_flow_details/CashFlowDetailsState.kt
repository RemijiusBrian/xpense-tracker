package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.AggregateAmountState

data class CashFlowDetailsState(
    val cashFlow: List<CashFlow> = emptyList(),
    val editMode: Boolean = false,
    val showAddCashFlowButton: Boolean = false,
    val aggregateAmount: String = "",
    val cashFlowStatus: AggregateAmountState? = null,
    val showClearCashFlowConfirmation: Boolean = false
) {
    companion object {
        val initial = CashFlowDetailsState()
    }
}