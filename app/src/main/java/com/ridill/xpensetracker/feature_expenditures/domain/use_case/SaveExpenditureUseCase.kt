package com.ridill.xpensetracker.feature_expenditures.domain.use_case

import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository

class SaveExpenditureUseCase(
    private val repository: ExpenditureRepository
) {
    suspend operator fun invoke(expenditure: Expenditure): Response<Long> {
        if (expenditure.name.isEmpty()) {
            return Response.Error(message = R.string.error_name_empty)
        }
        /*if (expense.amount <= 0) {
            return Response.Error(message = R.string.error_amount_invalid)
        }*/
        return Response.Success(repository.cacheExpenditure(expenditure))
    }
}