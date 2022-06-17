package com.xpenses.android.feature_expenses.data.repository

import com.xpenses.android.core.util.Constants
import com.xpenses.android.core.util.DispatcherProvider
import com.xpenses.android.feature_expenses.data.local.ExpenseDao
import com.xpenses.android.feature_expenses.data.local.ExpenseTagDao
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseTagEntity
import com.xpenses.android.feature_expenses.data.mapper.toEntity
import com.xpenses.android.feature_expenses.data.mapper.toExpense
import com.xpenses.android.feature_expenses.data.mapper.toExpenseListItem
import com.xpenses.android.feature_expenses.data.mapper.toMonthAndExpenditurePercent
import com.xpenses.android.feature_expenses.domain.model.Expense
import com.xpenses.android.feature_expenses.domain.model.ExpenseListItem
import com.xpenses.android.feature_expenses.domain.model.MonthAndExpenditurePercent
import com.xpenses.android.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao,
    private val tagDao: ExpenseTagDao,
    private val dispatcherProvider: DispatcherProvider
) : ExpenseRepository {

    override fun getMonthAndExpenditurePercentList(
        limit: Long
    ): Flow<List<MonthAndExpenditurePercent>> =
        expenseDao.getMonthAndExpenditureList().map { relations ->
            relations.map { it.toMonthAndExpenditurePercent(limit) }
        }

    override fun getTagsList(): Flow<List<String>> = tagDao.getAllTags()

    override fun getExpensesListForMonthFilteredByTag(
        month: String,
        tag: String
    ): Flow<List<ExpenseListItem>> = expenseDao.getExpensesForMonth(month).map { entities ->
        if (tag != Constants.STRING_ALL)
            entities.filter { it.tag == tag }
        else entities
    }.map { entities ->
        entities.map { it.toExpenseListItem() }
    }

    override suspend fun doesExpensesForTagExist(tag: String): Boolean =
        withContext(dispatcherProvider.io) {
            expenseDao.getExpensesByTag(tag).isNotEmpty()
        }

    override suspend fun getExpenseById(id: Long): Expense? = withContext(dispatcherProvider.io) {
        expenseDao.getExpenseById(id)?.toExpense()
    }

    override suspend fun cacheExpense(expense: Expense): Long = withContext(dispatcherProvider.io) {
        expenseDao.insert(expense.toEntity())
    }

    override suspend fun deleteExpenseById(id: Long) = withContext(dispatcherProvider.io) {
        expenseDao.deleteById(id)
    }

    override suspend fun cacheTag(tag: String) = withContext(dispatcherProvider.io) {
        tagDao.insert(ExpenseTagEntity(tag))
    }

    override suspend fun deleteTag(tag: String) = withContext(dispatcherProvider.io) {
        tagDao.deleteTag(tag)
    }

    override suspend fun deleteTagWithExpenses(tag: String) = withContext(dispatcherProvider.io) {
        tagDao.deleteTagWithExpenses(tag)
    }
}

