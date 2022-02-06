package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository

class DoesExpenseAlreadyExistUseCase(
    private val expenditureRepository: ExpenditureRepository
) {
    suspend operator fun invoke(name: String): Boolean =
        expenditureRepository.getExpenditureByName(name) != null
}