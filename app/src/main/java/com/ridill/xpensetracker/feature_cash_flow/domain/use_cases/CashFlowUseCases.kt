package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

data class CashFlowUseCases(
    val getAgents: GetAgentsUseCase,
    val getTotalCashFlowAmount: GetTotalCashFlowAmountUseCase
)