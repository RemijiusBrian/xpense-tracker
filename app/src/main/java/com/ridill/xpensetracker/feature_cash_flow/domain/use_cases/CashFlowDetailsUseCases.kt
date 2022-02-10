package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

data class CashFlowDetailsUseCases(
    val getAgentById: GetAgentByIdUseCase,
    val doesAgentAlreadyExist: DoesAgentAlreadyExistUseCase,
    val getAgentByName: GetAgentByNameUseCase,
    val saveOrGetExistingAgent: SaveOrGetExistingAgentUseCase,
    val getCashFlow: GetCashFlowForExpenseUseCase,
    val saveCashFlow: SaveCashFlowUseCase,
    val deleteCashFlow: DeleteCashFlowUseCase,
    val getCashFlowAggregate: GetCashFlowAggregateUseCase,
    val mapToCashFlowStatus: MapToCashFlowStatusUseCase,
    val getCashFlowById: GetCashFlowByIdUseCase,
    val clearCashFlowWithAgent: ClearCashFlowWithAgentUseCase
)