package dev.ridill.xpensetracker.feature_expenses.domain.repository

import dev.ridill.xpensetracker.feature_expenses.domain.model.Expense
import dev.ridill.xpensetracker.feature_expenses.domain.model.ExpenseListItem
import dev.ridill.xpensetracker.feature_expenses.domain.model.MonthAndExpenditure
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getMonthAndExpenditurePercentList(
        limit: Long
    ): Flow<List<MonthAndExpenditure>>

    fun getTagsList(): Flow<List<String>>

    fun getExpensesListForMonthFilteredByTag(
        month: String,
        tag: String
    ): Flow<List<ExpenseListItem>>

    suspend fun doesExpensesForTagExist(tag: String): Boolean

    suspend fun getExpenseById(id: Long): Expense?

    suspend fun cacheExpense(expense: Expense): Long

    suspend fun deleteExpenseById(id: Long)

    suspend fun cacheTag(tag: String)

    suspend fun deleteTag(tag: String)

    suspend fun deleteTagWithExpenses(tag: String)
}