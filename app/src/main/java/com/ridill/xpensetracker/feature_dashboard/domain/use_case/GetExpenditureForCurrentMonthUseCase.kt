package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpenditureForCurrentMonthUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<Long> = repository.getCurrentExpenditureForMonth()
}