package com.xpenses.android.feature_expenses.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.xpenses.android.core.data.local.db.XTDatabase
import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.core.util.Constants
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseEntity
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseTagEntity
import com.xpenses.android.feature_expenses.data.local.relation.MonthAndExpenditureRelation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ExpenseDaoTest {

    private lateinit var database: XTDatabase
    private lateinit var dao: ExpenseDao
    private lateinit var tagDao: ExpenseTagDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            XTDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.expenseDao
        tagDao = database.expenseTagDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertExpense() = runTest {
        val currentDate = System.currentTimeMillis()
        val expenseEntity = ExpenseEntity(
            id = 1L,
            name = "Test",
            amount = 100.0,
            dateMillis = currentDate,
            isMonthly = false
        )

        dao.insert(expenseEntity)

        val allExpenses = dao.getAllExpensesForMonth(
            TextUtil.formatDateWithPattern(
                currentDate,
                Constants.MONTH_YEAR_DB_PATTERN
            )
        ).first()

        assertThat(allExpenses).contains(expenseEntity)
    }

    @Test
    fun getMonthAndExpenditureList() = runTest {
        val currentDate = System.currentTimeMillis()
        val amount = 100.0
        val expenseEntity = ExpenseEntity(
            id = 1L,
            name = "Test",
            amount = amount,
            dateMillis = currentDate,
            isMonthly = false
        )

        dao.insert(expenseEntity)

        val monthAndExpenditureList = dao.getMonthAndExpenditureList().first()
        assertThat(monthAndExpenditureList)
            .contains(
                MonthAndExpenditureRelation(
                    TextUtil.formatDateWithPattern(
                        currentDate,
                        Constants.MONTH_YEAR_DB_PATTERN
                    ), amount
                )
            )
    }

    @Test
    fun getExpensesByTag() = runTest {
        val currentDate = System.currentTimeMillis()
        val tag = "Test Tag"
        tagDao.insert(ExpenseTagEntity(tag))
        val expenseEntity = ExpenseEntity(
            id = 1L,
            name = "Test",
            amount = 100.0,
            dateMillis = currentDate,
            isMonthly = false,
            tag = tag
        )
        dao.insert(expenseEntity)

        val expenses = dao.getExpensesByTag(tag)

        assertThat(expenses).contains(expenseEntity)
    }

    @Test
    fun updateExpense() = runTest {
        val currentDate = System.currentTimeMillis()
        val expenseEntity = ExpenseEntity(
            id = 1L,
            name = "Test",
            amount = 100.0,
            dateMillis = currentDate,
            isMonthly = false
        )
        dao.insert(expenseEntity)
        val list = dao.getAllExpensesForMonth(
            TextUtil.formatDateWithPattern(currentDate, Constants.MONTH_YEAR_DB_PATTERN)
        ).first()
        assertThat(list).contains(expenseEntity)
        val updatedExpense = expenseEntity.copy(
            name = "Updated Test",
            amount = 1000.0
        )
        dao.insert(updatedExpense)
        val listAfterUpdate = dao.getAllExpensesForMonth(
            TextUtil.formatDateWithPattern(currentDate, Constants.MONTH_YEAR_DB_PATTERN)
        ).first()
        assertThat(listAfterUpdate).doesNotContain(expenseEntity)
        assertThat(listAfterUpdate).contains(updatedExpense)
    }

    @Test
    fun deleteExpense() = runTest {
        val currentDate = System.currentTimeMillis()
        val expenseEntity = ExpenseEntity(
            id = 1L,
            name = "Test",
            amount = 100.0,
            dateMillis = currentDate,
            isMonthly = false
        )
        dao.insert(expenseEntity)
        val expenseList = dao.getAllExpensesForMonth(
            TextUtil.formatDateWithPattern(currentDate, Constants.MONTH_YEAR_DB_PATTERN)
        ).first()
        assertThat(expenseList).contains(expenseEntity)
        dao.deleteById(expenseEntity.id)
        val expenseListAfterDeletion = dao.getAllExpensesForMonth(
            TextUtil.formatDateWithPattern(currentDate, Constants.MONTH_YEAR_DB_PATTERN)
        ).first()
        assertThat(expenseListAfterDeletion).doesNotContain(expenseEntity)
    }
}