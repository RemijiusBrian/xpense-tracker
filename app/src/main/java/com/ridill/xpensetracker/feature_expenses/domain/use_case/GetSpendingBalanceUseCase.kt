package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSpendingBalanceUseCase(
    private val repository: ExpenseRepository,
    private val preferencesManager: XTPreferencesManager,
    private val cashFlowRepository: CashFlowRepository
) {
    operator fun invoke(): Flow<Long> = combineTuple(
        repository.getExpenditureForCurrentMonth(),
        preferencesManager.preferences,
        cashFlowRepository.getTotalCashFlowAmount()
    ).map { (expenditure, preference, cashFlowAmount) ->
        preference.expenditureLimit -
                (if (preference.cashFlowIncludedInExpenditure) expenditure + cashFlowAmount else expenditure)
    }
}