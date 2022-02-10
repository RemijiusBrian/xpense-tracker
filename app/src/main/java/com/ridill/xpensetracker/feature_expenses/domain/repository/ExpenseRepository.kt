package com.ridill.xpensetracker.feature_expenses.domain.repository

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getMothsOfExpenses(): Flow<List<String>>

    fun getExpensesForMonth(month: String): Flow<List<Expense>>

    fun getExpenditureForCurrentMonth(): Flow<Long>

    suspend fun getExpenseById(id: Long): Expense?

    suspend fun getExpenseByName(name: String): Expense?

    suspend fun cacheExpense(expense: Expense): Long

    suspend fun deleteExpense(expense: Expense)
}