package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.data.repository.CashFlowRepositoryImpl
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_cash_flow.domain.use_cases.*
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
    fun provideCashFlowDao(database: XTDatabase): CashFlowDao =
        database.cashFlowDao

    @Singleton
    @Provides
    fun provideCashFlowRepository(
        dao: CashFlowDao,
    ): CashFlowRepository = CashFlowRepositoryImpl(dao)

    // CashFlow Use Cases
    @Singleton
    @Provides
    fun provideCashFlowUseCases(
        repository: CashFlowRepository
    ): CashFlowUseCases = CashFlowUseCases(
        getAgents = GetAgentsUseCase(repository),
        getTotalCashFlowAmount = GetTotalCashFlowAmountUseCase(repository)
    )

    // CashFlow Details Use Cases
    @Singleton
    @Provides
    fun provideCashFlowDetailsUseCases(
        repository: CashFlowRepository,
    ): CashFlowDetailsUseCases = CashFlowDetailsUseCases(
        getAgentById = GetAgentByIdUseCase(repository),
        doesAgentAlreadyExist = DoesAgentAlreadyExistUseCase(repository),
        getAgentByName = GetAgentByNameUseCase(repository),
        saveOrGetExistingAgent = SaveOrGetExistingAgentUseCase(repository),
        getCashFlow = GetCashFlowForExpenseUseCase(repository),
        saveCashFlow = SaveCashFlowUseCase(repository),
        deleteCashFlow = DeleteCashFlowUseCase(repository),
        getCashFlowAggregate = GetCashFlowAggregateUseCase(repository),
        mapToCashFlowStatus = MapToCashFlowStatusUseCase(),
        getCashFlowById = GetCashFlowByIdUseCase(repository),
        clearCashFlowWithAgent = ClearCashFlowWithAgentUseCase(repository)
    )
}