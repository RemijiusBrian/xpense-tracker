package com.ridill.xpensetracker.di

import android.app.Application
import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpensePreferencesManager
import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpensePreferencesManagerImpl
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

    // Expense Preferences
    @Singleton
    @Provides
    fun provideExpensePreferences(application: Application): ExpensePreferencesManager =
        ExpensePreferencesManagerImpl(application)

    // Expense Repository
    @Singleton
    @Provides
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository = ExpenseRepositoryImpl(dao)

    // Expenses Use Cases
    @Singleton
    @Provides
    fun provideExpensesUseCases(
        repository: ExpenseRepository,
        expensePreferencesManager: ExpensePreferencesManager
    ): ExpensesUseCases = ExpensesUseCases(
        getMonthsList = GetMonthListUseCase(repository),
        getExpensesForMonth = GetExpensesForMonthUseCase(repository),
        getExpenditureForCurrentMonth = GetExpenditureForCurrentMonthUseCase(repository),
        getExpensePreferences = GetExpensePreferencesUseCase(expensePreferencesManager),
        deleteExpense = DeleteExpenseUseCase(repository),
        saveExpense = SaveExpenseUseCase(repository),
        getSpendingBalance = GetSpendingBalanceUseCase(repository, expensePreferencesManager),
        updateExpenditureLimit = UpdateExpenditureLimitUseCase(expensePreferencesManager)
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