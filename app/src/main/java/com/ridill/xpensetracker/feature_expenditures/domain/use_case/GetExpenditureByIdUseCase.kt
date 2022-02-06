package com.ridill.xpensetracker.feature_expenditures.domain.use_case

import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository

class GetExpenditureByIdUseCase(
    private val repository: ExpenditureRepository
) {
    suspend operator fun invoke(expenseId: Long): Expenditure? = repository.getExpenditureById(expenseId)
}