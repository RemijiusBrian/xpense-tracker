package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.core.util.DispatcherProvider
import com.ridill.xpensetracker.feature_expenses.data.local.TagsDao
import com.ridill.xpensetracker.feature_expenses.data.repository.TagsRepositoryImpl
import com.ridill.xpensetracker.feature_expenses.domain.repository.TagsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TagsModule {

    @Singleton
    @Provides
    fun provideTagsDao(database: XTDatabase): TagsDao = database.tagsDao

    @Singleton
    @Provides
    fun provideTagsRepository(
        dao: TagsDao,
        dispatcherProvider: DispatcherProvider
    ): TagsRepository = TagsRepositoryImpl(dao, dispatcherProvider)
}