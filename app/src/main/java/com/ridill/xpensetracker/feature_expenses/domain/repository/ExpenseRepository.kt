package com.ridill.xpensetracker.feature_expenses.domain.repository

import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getDates(): Flow<List<String>>

    fun getExpensesForMonth(month: String): Flow<List<Expense>>

    fun getExpenditureForCurrentMonth(): Flow<Long>

    suspend fun getExpenseById(id: Long): Expense?

    suspend fun getExpenseByName(name: String): Expense?

    suspend fun cacheExpense(expense: Expense): Long

    suspend fun deleteExpense(expense: Expense)

    fun getAllTags(): Flow<List<ExpenseTag>>

    suspend fun cacheTag(tag: ExpenseTag)

    suspend fun deleteTag(name: String)
}