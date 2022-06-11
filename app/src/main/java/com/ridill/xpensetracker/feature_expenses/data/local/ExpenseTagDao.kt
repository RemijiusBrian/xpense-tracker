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

    @Query("UPDATE ExpenseEntity SET tag = NULL WHERE tag = :tag")
    suspend fun resetTagsForExpensesWithTag(tag: String)

    @Query("DELETE FROM ExpenseTagEntity WHERE name = :tag")
    suspend fun deleteTag(tag: String)

    @Transaction
    suspend fun resetExpenseTagsAndDeleteTag(tag: String) {
        resetTagsForExpensesWithTag(tag)
        deleteTag(tag)
    }
}