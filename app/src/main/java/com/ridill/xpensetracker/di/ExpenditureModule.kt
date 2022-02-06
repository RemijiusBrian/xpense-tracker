package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.feature_expenditures.data.local.ExpenditureDao
import com.ridill.xpensetracker.feature_expenditures.data.repository.ExpenditureRepositoryImpl
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.AddEditExpenditureUseCases
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.DeleteExpenditureUseCase
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.GetExpenditureByIdUseCase
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.SaveExpenditureUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpenditureModule {

    // Expenditure Dao
    @Singleton
    @Provides
    fun provideExpenditureDao(database: XTDatabase): ExpenditureDao = database.expenditureDao

    // Expenditure Repository
    @Singleton
    @Provides
    fun provideExpenditureRepository(dao: ExpenditureDao): ExpenditureRepository =
        ExpenditureRepositoryImpl(dao)

    // Add/Edit Expenditure Use Cases
    @Singleton
    @Provides
    fun provideAddEditExpenditureUseCases(
        repository: ExpenditureRepository
    ): AddEditExpenditureUseCases = AddEditExpenditureUseCases(
        getExpenditureById = GetExpenditureByIdUseCase(repository),
        saveExpenditure = SaveExpenditureUseCase(repository),
        deleteExpenditure = DeleteExpenditureUseCase(repository)
    )
}