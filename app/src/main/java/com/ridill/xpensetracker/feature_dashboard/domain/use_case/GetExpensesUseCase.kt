package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_dashboard.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class GetExpensesUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(
        category: ExpenseCategory,
        showAll: Boolean,
    ): Flow<Map<String, List<Expense>>> =
        repository.getExpenses(category, showAll).map { expenses ->
            expenses.groupBy {
                SimpleDateFormat("MMM - yyyy", Locale.getDefault()).format(it.date)
            }
        }
}
