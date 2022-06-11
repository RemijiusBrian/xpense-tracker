package com.ridill.xpensetracker.feature_expenses.domain.repository

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseListItem
import com.ridill.xpensetracker.feature_expenses.domain.model.MonthAndExpenditurePercent
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getMonthAndExpenditurePercentList(
        limit: Long
    ): Flow<List<MonthAndExpenditurePercent>>

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