package com.ridill.xpensetracker.feature_expenses.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM ExpenseEntity WHERE category = :category")
    fun getAllExpensesForCategory(category: ExpenseCategory): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT *
        FROM ExpenseEntity
        WHERE (date BETWEEN :start AND :end OR isMonthly = 1) AND category = :category
        """
    )
    fun getExpensesForMonth(
        start: Long,
        end: Long,
        category: ExpenseCategory
    ): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT SUM(amount)
        FROM ExpenseEntity
        WHERE (date BETWEEN :start AND :end OR isMonthly = 1) AND category != :category
        """
    )
    fun getExpenditureForMonth(
        start: Long,
        end: Long,
        category: ExpenseCategory
    ): Flow<Long?>

    @Query("SELECT * FROM ExpenseEntity WHERE id = :expenseId")
    suspend fun getExpenseById(expenseId: Long): ExpenseEntity?

    @Query("SELECT * FROM ExpenseEntity WHERE name = :name LIMIT 1")
    suspend fun getExpenseByName(name: String): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseEntity: ExpenseEntity): Long

    @Delete
    fun delete(expenseEntity: ExpenseEntity)
}