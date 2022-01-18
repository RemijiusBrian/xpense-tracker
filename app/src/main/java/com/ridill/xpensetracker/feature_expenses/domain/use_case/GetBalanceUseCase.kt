package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpenseStore
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBalanceUseCase(
    private val repository: ExpenseRepository,
    private val expenseStore: ExpenseStore
) {
    operator fun invoke(): Flow<Long> = combineTuple(
        repository.getCurrentExpenditureForMonth(),
        expenseStore.preferences
    ).map { (expenditure, preference) ->
        preference.expenditureLimit - expenditure
    }
}