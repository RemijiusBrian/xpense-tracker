package com.ridill.xpensetracker.feature_expenses.domain.use_case

data class AddEditUseCases(
    val getExpenseById: GetExpenseByIdUseCase,
    val saveExpense: SaveExpenseUseCase,
    val deleteExpense: DeleteExpenseUseCase
)
