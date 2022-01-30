package com.ridill.xpensetracker.feature_expenses.domain.repository

import com.ridill.xpensetracker.feature_dashboard.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getExpenses(category: ExpenseCategory, showAll: Boolean): Flow<List<Expense>>

    fun getCurrentExpenditureForMonth(): Flow<Long>

    suspend fun getExpenseById(expenseId: Long): Expense?

    suspend fun getExpenseByName(name: String): Expense?

    suspend fun cacheExpense(expense: Expense): Long

    suspend fun deleteExpense(expense: Expense)
}