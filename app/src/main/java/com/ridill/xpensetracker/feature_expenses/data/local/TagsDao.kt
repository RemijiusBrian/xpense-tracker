package com.ridill.xpensetracker.feature_expenses.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {

    @Query("SELECT * FROM ExpenseTagEntity ORDER BY name ASC")
    fun getAllTags(): Flow<List<ExpenseTagEntity>>

    @Insert
    suspend fun insert(tag: ExpenseTagEntity)

    @Update
    suspend fun update(tag: ExpenseTagEntity)

    @Query("DELETE FROM ExpenseTagEntity WHERE name = :name")
    suspend fun delete(name: String)
}