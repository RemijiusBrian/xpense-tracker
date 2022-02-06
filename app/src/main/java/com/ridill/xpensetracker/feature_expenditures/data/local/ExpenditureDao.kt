package com.ridill.xpensetracker.feature_expenditures.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_expenditures.data.local.entity.ExpenditureEntity
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureDao {

    @Query("SELECT * FROM ExpenditureEntity WHERE category = :category ORDER BY dateMillis DESC")
    fun getExpenses(category: ExpenditureCategory): Flow<List<ExpenditureEntity>>

    @Query(
        """
        SELECT SUM(amount)
        FROM ExpenditureEntity
        WHERE (strftime('%m', dateMillis / 1000, 'unixepoch') = strftime('%m', date('now')) OR isMonthly = 1)
        AND category != :category
        """
    )
    fun getExpenditureForCurrentMonth(category: ExpenditureCategory): Flow<Long?>

    @Query("SELECT * FROM ExpenditureEntity WHERE id = :id")
    suspend fun getExpenditureById(id: Long): ExpenditureEntity?

    @Query("SELECT * FROM ExpenditureEntity WHERE name = :name LIMIT 1")
    suspend fun getExpenditureByName(name: String): ExpenditureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExpenditureEntity): Long

    @Delete
    fun delete(entity: ExpenditureEntity)
}