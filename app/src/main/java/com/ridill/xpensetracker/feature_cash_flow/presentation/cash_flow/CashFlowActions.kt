package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent

interface CashFlowActions {
    fun onAddCashFlowClick()
    fun onAgentClick(agent: CashFlowAgent)
}