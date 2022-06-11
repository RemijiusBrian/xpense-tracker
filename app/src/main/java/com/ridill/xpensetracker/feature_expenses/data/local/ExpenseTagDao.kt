package com.ridill.xpensetracker.feature_expenses.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseTagDao {

    @Query("SELECT name FROM ExpenseTagEntity ORDER BY name ASC")
    fun getAllTags(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExpenseTagEntity)

    @Query("DELETE FROM ExpenseTagEntity WHERE name = :tag")
    suspend fun deleteTag(tag: String)

    @Query("DELETE FROM ExpenseEntity WHERE tag = :tag")
    suspend fun deleteExpensesForTag(tag: String)

    @Transaction
    suspend fun deleteTagWithExpenses(tag: String) {
        deleteExpensesForTag(tag)
        deleteTag(tag)
    }
}