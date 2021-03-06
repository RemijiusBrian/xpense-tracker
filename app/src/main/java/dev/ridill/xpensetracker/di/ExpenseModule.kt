package dev.ridill.xpensetracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ridill.xpensetracker.core.data.local.db.XTDatabase
import dev.ridill.xpensetracker.core.util.DispatcherProvider
import dev.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import dev.ridill.xpensetracker.feature_expenses.data.local.ExpenseTagDao
import dev.ridill.xpensetracker.feature_expenses.data.repository.ExpenseRepositoryImpl
import dev.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
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