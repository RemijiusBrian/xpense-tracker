package com.ridill.xpensetracker.feature_expenses.data.repository

import com.ridill.xpensetracker.core.util.DispatcherProvider
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val dao: ExpenseDao,
    private val dispatcherProvider: DispatcherProvider
) : ExpenseRepository {

    override fun getDates(): Flow<List<String>> = dao.getDatesList()

    override fun getExpensesForMonth(month: String): Flow<List<Expense>> =
        dao.getExpenses(month).map { entities ->
            entities.map { it.toExpense() }
        }

    override fun getExpenditureForCurrentMonth(): Flow<Long> = dao.getExpenditureForCurrentMonth()

    override suspend fun getExpenseById(id: Long): Expense? = withContext(dispatcherProvider.io) {
        dao.getExpenseById(id)?.toExpense()
    }

    override suspend fun getExpenseByName(name: String): Expense? =
        withContext(dispatcherProvider.io) {
            dao.getExpenseByName(name)?.toExpense()
        }

    override suspend fun cacheExpense(expense: Expense): Long = withContext(dispatcherProvider.io) {
        dao.insert(expense.toEntity())
    }

    override suspend fun deleteExpense(expense: Expense) = withContext(dispatcherProvider.io) {
        dao.delete(expense.toEntity())
    }
}

