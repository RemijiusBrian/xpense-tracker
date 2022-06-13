package com.xpenses.android.di

import com.xpenses.android.core.data.local.db.XTDatabase
import com.xpenses.android.core.util.DispatcherProvider
import com.xpenses.android.feature_expenses.data.local.ExpenseDao
import com.xpenses.android.feature_expenses.data.local.ExpenseTagDao
import com.xpenses.android.feature_expenses.data.repository.ExpenseRepositoryImpl
import com.xpenses.android.feature_expenses.domain.repository.ExpenseRepository
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