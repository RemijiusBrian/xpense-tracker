package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpensePreferencesManager
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSpendingBalanceUseCase(
    private val repository: ExpenseRepository,
    private val expenseStore: ExpensePreferencesManager,
    private val cashFlowRepository: CashFlowRepository
) {
    operator fun invoke(): Flow<Long> = combineTuple(
        repository.getExpenditureForCurrentMonth(),
        expenseStore.preferences,
        cashFlowRepository.getTotalCashFlowAmount()
    ).map { (expenditure, preference, cashFlowAmount) ->
        preference.expenditureLimit -
                (if (preference.cashFlowIncludedInExpenditure) expenditure + cashFlowAmount else expenditure)
    }
}