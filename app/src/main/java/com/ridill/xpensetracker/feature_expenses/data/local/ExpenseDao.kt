package com.ridill.xpensetracker.feature_expenses.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query(
        """
        SELECT DISTINCT(strftime('%m-%Y', dateMillis / 1000, 'unixepoch'))
        FROM ExpenseEntity 
        ORDER BY dateMillis DESC
        """
    )
    fun getDatesList(): Flow<List<String>>

    @Query(
        """
        SELECT *
        FROM ExpenseEntity
        WHERE (strftime('%m-%Y', dateMillis / 1000, 'unixepoch') = :date OR isMonthly = 1)
        ORDER BY isMonthly DESC, dateMillis DESC
    """
    )
    fun getExpenses(
        date: String
    ): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT IFNULL(SUM(amount), 0)
        FROM ExpenseEntity
        WHERE (strftime('%m', dateMillis / 1000, 'unixepoch') = strftime('%m', date('now')) OR isMonthly = 1)
        """
    )
    fun getExpenditureForCurrentMonth(): Flow<Long>

    @Query("SELECT * FROM ExpenseEntity WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseEntity: ExpenseEntity): Long

    @Delete
    fun delete(expenseEntity: ExpenseEntity)
}