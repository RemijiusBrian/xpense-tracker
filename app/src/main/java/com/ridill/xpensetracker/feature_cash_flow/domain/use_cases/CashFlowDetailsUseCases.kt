package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_expenditures.domain.use_case.GetExpenditureByIdUseCase
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.SaveExpenditureUseCase

data class CashFlowDetailsUseCases(
    val getExpenditureById: GetExpenditureByIdUseCase,
    val doesExpenseAlreadyExist: DoesExpenseAlreadyExistUseCase,
    val getExpenseByName: GetExpenseByNameUseCase,
    val saveExpenditure: SaveExpenditureUseCase,
    val getCashFlow: GetCashFlowForExpenseUseCase,
    val saveCashFlow: SaveCashFlowUseCase,
    val deleteCashFlow: DeleteCashFlowUseCase,
    val getOverallAmount: GetOverallAmountUseCase,
    val mapToCashFlowStatus: MapToCashFlowStatusUseCase,
    val getCashFlowById: GetCashFlowByIdUseCase,
    val strikeOffCashFlow: StrikeOffCashFlowUseCase
)