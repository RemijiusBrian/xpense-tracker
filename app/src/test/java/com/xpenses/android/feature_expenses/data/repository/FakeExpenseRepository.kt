package com.xpenses.android.feature_expenses.data.repository

import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.core.util.Constants
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseEntity
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseTagEntity
import com.xpenses.android.feature_expenses.data.mapper.toEntity
import com.xpenses.android.feature_expenses.data.mapper.toExpense
import com.xpenses.android.feature_expenses.data.mapper.toExpenseListItem
import com.xpenses.android.feature_expenses.domain.model.Expense
import com.xpenses.android.feature_expenses.domain.model.ExpenseListItem
import com.xpenses.android.feature_expenses.domain.model.MonthAndExpenditurePercent
import com.xpenses.android.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeExpenseRepository : ExpenseRepository {

    private val expensesList = mutableListOf<ExpenseEntity>()
    private val expenseListFlow = flowOf(expensesList.toList())
    private val tagsList = mutableListOf<ExpenseTagEntity>()
    private val tagsListFlow = flowOf(tagsList.toList())

    override fun getMonthAndExpenditurePercentList(limit: Long): Flow<List<MonthAndExpenditurePercent>> {
        return expenseListFlow.map { list ->
            list.distinctBy {
                TextUtil.formatDateWithPattern(
                    it.dateMillis,
                    Constants.MONTH_YEAR_UI_PATTERN
                )
            }
        }.map { list ->
            list.map {
                MonthAndExpenditurePercent(
                    TextUtil.formatDateWithPattern(it.dateMillis, Constants.MONTH_YEAR_UI_PATTERN),
                    expenditurePercent = (it.amount / limit).toFloat()
                )
            }
        }
    }

    override fun getTagsList(): Flow<List<String>> {
        return tagsListFlow.map { list -> list.map { it.name } }
    }

    override fun getExpensesListForMonthFilteredByTag(
        month: String,
        tag: String
    ): Flow<List<ExpenseListItem>> {
        return expenseListFlow.map { list -> list.map { it.toExpenseListItem() } }
    }

    override suspend fun doesExpensesForTagExist(tag: String): Boolean {
        return expensesList.find { it.tag == tag } != null
    }

    override suspend fun getExpenseById(id: Long): Expense? {
        return expensesList.find { it.id == id }?.toExpense()
    }

    override suspend fun cacheExpense(expense: Expense): Long {
        expensesList.add(expense.toEntity())
        return expense.id
    }

    override suspend fun deleteExpenseById(id: Long) {
        expensesList.removeIf { it.id == id }
    }

    override suspend fun cacheTag(tag: String) {
        tagsList.add(ExpenseTagEntity(tag))
    }

    override suspend fun deleteTag(tag: String) {
        tagsList.removeIf { it.name == tag }
    }

    override suspend fun deleteTagWithExpenses(tag: String) {
        expensesList.removeIf { it.tag == tag }
        tagsList.removeIf { it.name == tag }
    }
}