package com.xpenses.android.di

import com.xpenses.android.core.data.local.db.XTDatabase
import com.xpenses.android.core.util.DispatcherProvider
import com.xpenses.android.feature_bills.data.local.BillsDao
import com.xpenses.android.feature_bills.data.repository.BillsRepositoryImpl
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import com.xpenses.android.feature_expenses.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BillsModule {

    @Provides
    fun provideBillsDao(database: XTDatabase): BillsDao = database.billsDao

    @Provides
    fun provideBillsRepository(
        dao: BillsDao,
        dispatcherProvider: DispatcherProvider,
        expenseRepository: ExpenseRepository
    ): BillsRepository = BillsRepositoryImpl(dao, dispatcherProvider, expenseRepository)
}