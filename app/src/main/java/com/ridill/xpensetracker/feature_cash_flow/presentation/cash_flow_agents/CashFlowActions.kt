package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents

interface CashFlowActions {
    fun onAddCashFlowClick()
    fun onAgentClick(agentId: Long)
    fun onSearchClick()
    fun onSearchDismiss()
    fun onSearchQueryClear()
    fun onSearchQueryChange(query: String)
}
