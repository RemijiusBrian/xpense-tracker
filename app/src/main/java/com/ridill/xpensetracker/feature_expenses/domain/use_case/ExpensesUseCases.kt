package com.ridill.xpensetracker.feature_expenses.domain.use_case

data class ExpensesUseCases(
    val getMonthsList: GetMonthListUseCase,
    val getExpensesForMonth: GetExpensesForMonthUseCase,
    val getExpenditureForCurrentMonth: GetExpenditureForCurrentMonthUseCase,
    val getSpendingBalance: GetSpendingBalanceUseCase,
    val getExpensePreferences: GetExpensePreferencesUseCase,
    val deleteExpense: DeleteExpenseUseCase,
    val saveExpense: SaveExpenseUseCase,
    val updateExpenditureLimit: UpdateExpenditureLimitUseCase
)