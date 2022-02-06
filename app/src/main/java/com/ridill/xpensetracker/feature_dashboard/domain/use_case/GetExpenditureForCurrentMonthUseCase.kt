package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository
import kotlinx.coroutines.flow.Flow

class GetExpenditureForCurrentMonthUseCase(
    private val repository: ExpenditureRepository
) {
    operator fun invoke(): Flow<Long> = repository.getTotalExpenditureAmountForCurrentMonth()
}