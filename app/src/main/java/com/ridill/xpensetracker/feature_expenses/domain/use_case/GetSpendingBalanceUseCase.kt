package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpensePreferencesManager
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSpendingBalanceUseCase(
    private val repository: ExpenseRepository,
    private val expenseStore: ExpensePreferencesManager
) {
    operator fun invoke(): Flow<Long> = combineTuple(
        repository.getExpenditureForCurrentMonth(),
        expenseStore.preferences
    ).map { (expenditure, preference) ->
        preference.expenditureLimit - expenditure
    }
}