package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository

class GetExpenseByNameUseCase(
    private val expenditureRepository: ExpenditureRepository
) {
    suspend operator fun invoke(name: String): Expenditure? =
        expenditureRepository.getExpenditureByName(name)
}