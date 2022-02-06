package com.ridill.xpensetracker.feature_expenses.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM ExpenseEntity WHERE category = :category ORDER BY dateMillis DESC")
    fun getExpenses(category: ExpenseCategory): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT SUM(amount)
        FROM ExpenseEntity
        WHERE (strftime('%m', dateMillis / 1000, 'unixepoch') = strftime('%m', date('now')) OR isMonthly = 1)
        AND category != :category
        """
    )
    fun getExpenditureForCurrentMonth(category: ExpenseCategory): Flow<Long?>

    @Query("SELECT * FROM ExpenseEntity WHERE id = :expenseId")
    suspend fun getExpenseById(expenseId: Long): ExpenseEntity?

    @Query("SELECT * FROM ExpenseEntity WHERE name = :name LIMIT 1")
    suspend fun getExpenseByName(name: String): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseEntity: ExpenseEntity): Long

    @Delete
    fun delete(expenseEntity: ExpenseEntity)
}