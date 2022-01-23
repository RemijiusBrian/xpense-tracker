package com.ridill.xpensetracker.di

import android.app.Application
import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpenseStore
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

    // Expense Store
    @Singleton
    @Provides
    fun provideExpenseStore(application: Application): ExpenseStore = ExpenseStore(application)

    // Expenses UseCases
    @Singleton
    @Provides
    fun provideMonthlyExpenseUseCases(
        repository: ExpenseRepository,
        expenseStore: ExpenseStore
    ): ExpensesUseCases = ExpensesUseCases(
        getExpenses = GetExpensesUseCase(repository),
        getExpenditureForCurrentMonth = GetExpenditureForCurrentMonthUseCase(repository),
        getExpensePreference = GetExpensePreferenceUseCase(expenseStore),
        updateExpenditureLimit = UpdateExpenditureLimitUseCase(expenseStore),
        deleteExpense = DeleteExpenseUseCase(repository),
        saveExpense = SaveExpenseUseCase(repository),
        updatePreferenceCategory = UpdatePreferenceCategoryUseCase(expenseStore),
        getBalance = GetBalanceUseCase(repository, expenseStore),
        updateShowPreviousEntries = UpdateShowPreviousEntriesUseCase(expenseStore)
    )

    // Add/Edit Expense Use Cases
    @Singleton
    @Provides
    fun provideAddEditUseCases(
        repository: ExpenseRepository
    ): AddEditUseCases = AddEditUseCases(
        getExpenseById = GetExpenseByIdUseCase(repository),
        saveExpense = SaveExpenseUseCase(repository),
        deleteExpense = DeleteExpenseUseCase(repository)
    )
}