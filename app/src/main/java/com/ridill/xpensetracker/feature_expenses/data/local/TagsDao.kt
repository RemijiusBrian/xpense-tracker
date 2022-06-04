package com.ridill.xpensetracker.feature_expenses.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity
import com.ridill.xpensetracker.feature_expenses.data.local.relations.TagsWithExpenses
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {

    @Query("SELECT * FROM ExpenseTagEntity ORDER BY name ASC")
    fun getAllTags(): Flow<List<ExpenseTagEntity>>

    @Transaction
    @Query("SELECT * FROM ExpenseTagEntity ORDER BY name ASC")
    fun getTagsWithExpenses(): Flow<List<TagsWithExpenses>>

    @Insert
    suspend fun insert(tag: ExpenseTagEntity)

    @Update
    suspend fun update(tag: ExpenseTagEntity)

    @Transaction
    suspend fun deleteTag(tag: ExpenseTagEntity) {
        removeTagFromExpenses(tag.name)
        delete(tag)
    }

    @Query("UPDATE ExpenseEntity SET tag = NULL WHERE tag = :tag")
    suspend fun removeTagFromExpenses(tag: String)

    @Delete
    suspend fun delete(tag: ExpenseTagEntity)
}