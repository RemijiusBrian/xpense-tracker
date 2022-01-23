package com.ridill.xpensetracker.feature_expenses.domain.use_case

data class ExpensesUseCases(
    val getExpenses: GetExpensesUseCase,
    val getExpenditureForCurrentMonth: GetExpenditureForCurrentMonthUseCase,
    val getBalance: GetBalanceUseCase,
    val updatePreferenceCategory: UpdatePreferenceCategoryUseCase,
    val getExpensePreference: GetExpensePreferenceUseCase,
    val updateExpenditureLimit: UpdateExpenditureLimitUseCase,
    val deleteExpense: DeleteExpenseUseCase,
    val saveExpense: SaveExpenseUseCase,
    val updateShowPreviousEntries: UpdateShowPreviousEntriesUseCase
)