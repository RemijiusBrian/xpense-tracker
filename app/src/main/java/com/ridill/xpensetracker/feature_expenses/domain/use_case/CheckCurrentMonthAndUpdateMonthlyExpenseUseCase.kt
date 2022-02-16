package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

class CheckCurrentMonthAndUpdateMonthlyExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke() {
        val latestMonth = repository.getMonthNames().first().first()

        val currentTimeMillis = System.currentTimeMillis()
        val currentMonth = SimpleDateFormat("MMMM-yyyy", Locale.getDefault())
            .format(Date(currentTimeMillis))

        if (latestMonth == currentMonth) return

        val monthlyExpenses = repository.getMonthlyExpenses()
        if (monthlyExpenses.isEmpty()) return

        monthlyExpenses.forEach { expense ->
            repository.cacheExpense(expense.copy(dateMillis = currentTimeMillis))
        }
    }
}