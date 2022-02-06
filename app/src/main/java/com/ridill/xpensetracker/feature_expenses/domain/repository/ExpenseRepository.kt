package com.ridill.xpensetracker.feature_expenses.domain.repository

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getExpenses(category: ExpenseCategory): Flow<List<Expense>>

    fun getExpenditureForCurrentMonth(): Flow<Long>

    suspend fun getExpenseById(id: Long): Expense?

    suspend fun getExpenseByName(name: String): Expense?

    suspend fun cacheExpense(expense: Expense): Long

    suspend fun deleteExpense(expense: Expense)
}