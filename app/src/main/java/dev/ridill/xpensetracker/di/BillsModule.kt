package dev.ridill.xpensetracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ridill.xpensetracker.core.data.local.db.XTDatabase
import dev.ridill.xpensetracker.core.notification.NotificationHelper
import dev.ridill.xpensetracker.core.util.DispatcherProvider
import dev.ridill.xpensetracker.feature_bills.data.local.BillsDao
import dev.ridill.xpensetracker.feature_bills.data.repository.BillsRepositoryImpl
import dev.ridill.xpensetracker.feature_bills.domain.model.Bill
import dev.ridill.xpensetracker.feature_bills.domain.notification.BillReminderNotificationHelper
import dev.ridill.xpensetracker.feature_bills.domain.repository.BillsRepository
import dev.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
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