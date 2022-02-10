package com.ridill.xpensetracker.feature_expenses.domain.use_case

data class AddEditExpenseUseCases(
    val getExpenseById: GetExpenseByIdUseCase,
    val saveExpense: SaveExpenseUseCase,
    val deleteExpense: DeleteExpenseUseCase
)
