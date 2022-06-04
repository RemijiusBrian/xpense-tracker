package com.ridill.xpensetracker.feature_expenses.data.repository

import com.ridill.xpensetracker.core.util.DispatcherProvider
import com.ridill.xpensetracker.feature_expenses.data.local.TagsDao
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag
import com.ridill.xpensetracker.feature_expenses.domain.model.TagsWithExpensesAndAmount
import com.ridill.xpensetracker.feature_expenses.domain.repository.TagsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TagsRepositoryImpl(
    private val dao: TagsDao,
    private val dispatcherProvider: DispatcherProvider
) : TagsRepository {

    override fun getAllTags(): Flow<List<ExpenseTag>> {
        return dao.getAllTags().map { tags -> tags.map { it.toTag() } }
    }

    override fun getTagsWithExpenses(): Flow<List<TagsWithExpensesAndAmount>> {
        return dao.getTagsWithExpenses().map { entities ->
            entities.map { it.toTagsWithExpensesAndAmount() }
        }
    }

    override suspend fun cacheTag(tag: ExpenseTag) = withContext(dispatcherProvider.io) {
        dao.insert(tag.toEntity())
    }

    override suspend fun updateTag(tag: ExpenseTag) = withContext(dispatcherProvider.io) {
        dao.update(tag.toEntity())
    }

    override suspend fun deleteTag(tag: ExpenseTag) = withContext(dispatcherProvider.io) {
        dao.deleteTag(tag.toEntity())
    }
}