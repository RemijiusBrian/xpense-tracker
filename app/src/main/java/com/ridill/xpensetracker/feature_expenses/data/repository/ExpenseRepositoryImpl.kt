package com.ridill.xpensetracker.feature_expenses.data.repository

import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val dao: ExpenseDao
) : ExpenseRepository {

    override fun getDates(): Flow<List<String>> =
        dao.getMonthsOfExpenses()

    override fun getExpensesForMonth(month: String): Flow<List<Expense>> =
        dao.getExpenses(month).map { entities ->
            entities.map { it.toExpense() }
        }

    override fun getExpenditureForCurrentMonth(): Flow<Long> =
        dao.getExpenditureForCurrentMonth()

    override suspend fun getExpenseById(id: Long): Expense? =
        dao.getExpenseById(id)?.toExpense()

    override suspend fun getExpenseByName(name: String): Expense? =
        dao.getExpenseByName(name)?.toExpense()

    override suspend fun cacheExpense(expense: Expense): Long =
        dao.insert(expense.toEntity())

    override suspend fun deleteExpense(expense: Expense) = withContext(Dispatchers.IO) {
        dao.delete(expense.toEntity())
    }

    override suspend fun getMonthlyExpenses(): List<Expense> =
        dao.getMonthlyExpenses().map { it.toExpense() }
}