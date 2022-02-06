package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.data.repository.CashFlowRepositoryImpl
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_cash_flow.domain.use_cases.*
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.GetExpenditureByIdUseCase
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.SaveExpenditureUseCase
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

    @Singleton
    @Provides
    fun provideCashFlowUseCases(
        cashFlowRepository: CashFlowRepository,
        expenditureRepository: ExpenditureRepository
    ): CashFlowDetailsUseCases = CashFlowDetailsUseCases(
        getExpenditureById = GetExpenditureByIdUseCase(expenditureRepository),
        doesExpenseAlreadyExist = DoesExpenseAlreadyExistUseCase(expenditureRepository),
        getExpenseByName = GetExpenseByNameUseCase(expenditureRepository),
        saveExpenditure = SaveExpenditureUseCase(expenditureRepository),
        getCashFlow = GetCashFlowForExpenseUseCase(cashFlowRepository),
        saveCashFlow = SaveCashFlowUseCase(cashFlowRepository),
        deleteCashFlow = DeleteCashFlowUseCase(cashFlowRepository),
        getOverallAmount = GetOverallAmountUseCase(cashFlowRepository),
        mapToCashFlowStatus = MapToCashFlowStatusUseCase(),
        getCashFlowById = GetCashFlowByIdUseCase(cashFlowRepository),
        strikeOffCashFlow = StrikeOffCashFlowUseCase(cashFlowRepository, expenditureRepository)
    )
}