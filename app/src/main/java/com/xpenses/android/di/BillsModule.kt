package com.xpenses.android.di

import android.content.Context
import com.xpenses.android.core.data.local.db.XTDatabase
import com.xpenses.android.core.notification.NotificationHelper
import com.xpenses.android.core.util.DispatcherProvider
import com.xpenses.android.feature_bills.data.local.BillsDao
import com.xpenses.android.feature_bills.data.repository.BillsRepositoryImpl
import com.xpenses.android.feature_bills.domain.model.Bill
import com.xpenses.android.feature_bills.domain.notification.BillReminderNotificationHelper
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import com.xpenses.android.feature_expenses.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Singleton
    @Provides
    fun provideBillReminderNotificationHelper(
        @ApplicationContext applicationContext: Context
    ): NotificationHelper<Bill> = BillReminderNotificationHelper(applicationContext)
}