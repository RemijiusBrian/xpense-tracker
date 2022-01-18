package com.ridill.xpensetracker.feature_expenses.data.repository

import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*

class ExpenseRepositoryImpl(
    private val dao: ExpenseDao
) : ExpenseRepository {

    override fun getExpenses(
        category: ExpenseCategory,
        showAll: Boolean
    ): Flow<List<Expense>> {
        return if (showAll) {
            dao.getAllExpensesForCategory(category)
        } else {
            val dateWithStartDay = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, getActualMinimum(Calendar.DATE))
            }.timeInMillis

            val dateWithEndDay = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DATE))
            }.timeInMillis
            dao.getExpensesForMonth(
                start = dateWithStartDay,
                end = dateWithEndDay,
                category = category
            )
        }.map { entities -> entities.map { it.toExpense() } }
    }

    override fun getCurrentExpenditureForMonth(): Flow<Long> {
        val dateWithStartDay = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMinimum(Calendar.DATE))
        }.timeInMillis

        val dateWithEndDay = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DATE))
        }.timeInMillis

        return dao.getExpenditureForMonth(
            dateWithStartDay,
            dateWithEndDay,
            ExpenseCategory.YEARNING
        ).map { it ?: 0L }
    }

    override suspend fun getExpenseById(expenseId: Long): Expense? =
        dao.getExpenseById(expenseId)?.toExpense()

    override suspend fun getExpenseByName(name: String): Expense? =
        dao.getExpenseByName(name)

    override suspend fun cacheExpense(expense: Expense): Long =
        dao.insert(expense.toEntity())

    override suspend fun deleteExpense(expense: Expense) = withContext(Dispatchers.IO) {
        dao.delete(expense.toEntity())
    }
}