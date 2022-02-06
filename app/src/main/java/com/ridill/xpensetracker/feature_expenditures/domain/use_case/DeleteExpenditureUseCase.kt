package com.ridill.xpensetracker.feature_expenditures.domain.use_case

import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository

class DeleteExpenditureUseCase(
    private val repository: ExpenditureRepository
) {
    suspend operator fun invoke(expenditure: Expenditure) = repository.deleteExpenditure(expenditure)
}