package com.ridill.xpensetracker.feature_expenses.domain.repository

import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag
import kotlinx.coroutines.flow.Flow

interface TagsRepository {

    fun getAllTags(): Flow<List<ExpenseTag>>

    suspend fun cacheTag(tag: ExpenseTag)

    suspend fun updateTag(tag: ExpenseTag)

    suspend fun deleteTag(tag: ExpenseTag)
}