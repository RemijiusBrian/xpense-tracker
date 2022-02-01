package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.data.preferences.ExpensePreferenceManager
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBalanceUseCase(
    private val repository: ExpenseRepository,
    private val expenseStore: ExpensePreferenceManager
) {
    operator fun invoke(): Flow<Long> = combineTuple(
        repository.getCurrentExpenditureForMonth(),
        expenseStore.preferences
    ).map { (expenditure, preference) ->
        preference.expenditureLimit - expenditure
    }
}