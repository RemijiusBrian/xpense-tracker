package com.ridill.xpensetracker.di

import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.core.domain.use_case.GetPreferencesUseCase
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.data.repository.ExpenseRepositoryImpl
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.ridill.xpensetracker.feature_expenses.domain.use_case.*
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

    // Expense Repository
    @Singleton
    @Provides
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository = ExpenseRepositoryImpl(dao)

    @Provides
    fun provideCheckCurrentMonthAndUpdateMonthlyExpenseUseCase(
        repository: ExpenseRepository
    ): CheckCurrentMonthAndUpdateMonthlyExpenseUseCase = CheckCurrentMonthAndUpdateMonthlyExpenseUseCase(repository)

    // Expenses Use Cases
    @Singleton
    @Provides
    fun provideExpensesUseCases(
        repository: ExpenseRepository,
        preferencesManager: XTPreferencesManager,
        cashFlowRepository: CashFlowRepository
    ): ExpensesUseCases = ExpensesUseCases(
        getMonthsList = GetMonthListUseCase(repository),
        getExpensesForMonth = GetExpensesForMonthUseCase(repository),
        getPreferences = GetPreferencesUseCase(preferencesManager),
        deleteExpense = DeleteExpenseUseCase(repository),
        saveExpense = SaveExpenseUseCase(repository),
        getSpendingBalance =
        GetSpendingBalanceUseCase(repository, preferencesManager, cashFlowRepository),
        updateExpenditureLimit = UpdateExpenditureLimitUseCase(preferencesManager),
        getExpenditureForCurrentMonth =
        GetExpenditureForCurrentMonthUseCase(repository, preferencesManager, cashFlowRepository)
    )

    // Add/Edit Expense Use Cases
    @Singleton
    @Provides
    fun provideAddEditUseCases(
        repository: ExpenseRepository
    ): AddEditExpenseUseCases = AddEditExpenseUseCases(
        getExpenseById = GetExpenseByIdUseCase(repository),
        saveExpense = SaveExpenseUseCase(repository),
        deleteExpense = DeleteExpenseUseCase(repository)
    )
}