package dev.ridill.xpensetracker.feature_expenses.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import dev.ridill.xpensetracker.feature_expenses.data.local.relation.MonthAndExpenditureRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query(
        """
        SELECT DISTINCT(strftime('%m-%Y', dateMillis / 1000, 'unixepoch')) as month, SUM(amount) as expenditure
        FROM ExpenseEntity 
        GROUP BY month
        ORDER BY month DESC
        """
    )
    fun getMonthAndExpenditureList(): Flow<List<MonthAndExpenditureRelation>>

    @Query(
        """
        SELECT *
        FROM ExpenseEntity
        WHERE strftime('%m-%Y', dateMillis / 1000, 'unixepoch') = :month
        ORDER BY dateMillis DESC
    """
    )
    fun getExpensesForMonth(
        month: String
    ): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM ExpenseEntity WHERE tag = :tag")
    suspend fun getExpensesByTag(tag: String): List<ExpenseEntity>

    @Query("SELECT * FROM ExpenseEntity WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseEntity: ExpenseEntity): Long

    @Query("DELETE FROM ExpenseEntity WHERE id = :id")
    fun deleteById(id: Long)
}