package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.core.util.DispatcherProvider
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseTagDao
import com.ridill.xpensetracker.feature_expenses.data.repository.ExpenseRepositoryImpl
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpenseModule {

    // Expense Dao
    @Singleton
    @Provides
    fun provideExpenseDao(database: XTDatabase): ExpenseDao = database.expenseDao

    @Singleton
    @Provides
    fun provideExpenseTagDao(database: XTDatabase): ExpenseTagDao = database.expenseTagDao

    // Expense Repository
    @Singleton
    @Provides
    fun provideExpenseRepository(
        expenseDao: ExpenseDao,
        tagDao: ExpenseTagDao,
        dispatcherProvider: DispatcherProvider
    ): ExpenseRepository = ExpenseRepositoryImpl(expenseDao, tagDao, dispatcherProvider)
}