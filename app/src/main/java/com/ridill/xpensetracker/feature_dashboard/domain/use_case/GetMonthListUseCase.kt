package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class GetMonthListUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<String>> {
        val dateParser = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        val dateFormatter = SimpleDateFormat("MMMM-yyyy", Locale.getDefault())
        return repository.getMothsOfExpenses().map { dates ->
            dates.map { dateParser.parse(it)?.let { date -> dateFormatter.format(date) }.orEmpty() }
        }
    }
}