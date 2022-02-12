package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.core.domain.use_case.GetPreferencesUseCase

data class ExpensesUseCases(
    val getMonthsList: GetMonthListUseCase,
    val getExpensesForMonth: GetExpensesForMonthUseCase,
    val getExpenditureForCurrentMonth: GetExpenditureForCurrentMonthUseCase,
    val getSpendingBalance: GetSpendingBalanceUseCase,
    val getPreferences: GetPreferencesUseCase,
    val deleteExpense: DeleteExpenseUseCase,
    val saveExpense: SaveExpenseUseCase,
    val updateExpenditureLimit: UpdateExpenditureLimitUseCase
)