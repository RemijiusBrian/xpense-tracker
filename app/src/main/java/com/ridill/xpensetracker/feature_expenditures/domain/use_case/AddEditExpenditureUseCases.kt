package com.ridill.xpensetracker.feature_expenditures.domain.use_case

data class AddEditExpenditureUseCases(
    val getExpenditureById: GetExpenditureByIdUseCase,
    val saveExpenditure: SaveExpenditureUseCase,
    val deleteExpenditure: DeleteExpenditureUseCase
)
