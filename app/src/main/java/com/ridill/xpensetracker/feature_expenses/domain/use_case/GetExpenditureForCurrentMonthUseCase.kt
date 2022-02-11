package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpensePreferencesManager
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExpenditureForCurrentMonthUseCase(
    private val expenseRepository: ExpenseRepository,
    private val expensePreferencesManager: ExpensePreferencesManager,
    private val cashFlowRepository: CashFlowRepository
) {
    operator fun invoke(): Flow<Long> = combineTuple(
        expenseRepository.getExpenditureForCurrentMonth(),
        expensePreferencesManager.preferences,
        cashFlowRepository.getTotalCashFlowAmount()
    ).map { (expenditure, preferences, cashFlowAmount) ->
        if (preferences.cashFlowIncludedInExpenditure) expenditure + cashFlowAmount
        else expenditure
    }
}