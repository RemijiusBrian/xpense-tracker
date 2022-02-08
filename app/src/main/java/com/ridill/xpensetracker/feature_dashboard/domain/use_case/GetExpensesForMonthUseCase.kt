package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.Month

class GetExpensesForMonthUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(
        category: ExpenseCategory,
        date: String
    ): Flow<List<Expense>> {
        val monthNumber = Month.valueOf(date.substringBefore("-").uppercase()).value
            .toString().padStart(2, '0')
        val monthFormatted = date.replaceBefore("-", monthNumber)

        return repository.getExpensesForMonth(category, monthFormatted)
    }
}