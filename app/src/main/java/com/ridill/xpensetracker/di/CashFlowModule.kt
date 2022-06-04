package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.core.util.DispatcherProvider
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowAgentDao
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.data.repository.CashFlowRepositoryImpl
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CashFlowModule {

    @Singleton
    @Provides
    fun provideCashFlowAgentDao(database: XTDatabase): CashFlowAgentDao =
        database.cashFlowAgentDao

    @Singleton
    @Provides
    fun provideCashFlowDao(database: XTDatabase): CashFlowDao =
        database.cashFlowDao

    @Singleton
    @Provides
    fun provideCashFlowRepository(
        agentDao: CashFlowAgentDao,
        cashFlowDao: CashFlowDao,
        dispatcherProvider: DispatcherProvider
    ): CashFlowRepository = CashFlowRepositoryImpl(agentDao, cashFlowDao, dispatcherProvider)
}