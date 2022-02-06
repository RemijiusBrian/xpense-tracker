package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_expenses.domain.use_case.GetExpenseByIdUseCase
import com.ridill.xpensetracker.feature_expenses.domain.use_case.SaveExpenseUseCase

data class CashFlowDetailsUseCases(
    val getExpenseById: GetExpenseByIdUseCase,
    val doesExpenseAlreadyExist: DoesExpenseAlreadyExistUseCase,
    val getExpenseByName: GetExpenseByNameUseCase,
    val saveExpense: SaveExpenseUseCase,
    val getCashFlow: GetCashFlowForExpenseUseCase,
    val saveCashFlow: SaveCashFlowUseCase,
    val deleteCashFlow: DeleteCashFlowUseCase,
    val getOverallAmount: GetOverallAmountUseCase,
    val mapToCashFlowStatus: MapToCashFlowStatusUseCase,
    val getCashFlowById: GetCashFlowByIdUseCase,
    val strikeOffCashFlow: StrikeOffCashFlowUseCase
)