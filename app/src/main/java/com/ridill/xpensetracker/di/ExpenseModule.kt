package com.ridill.xpensetracker.di

import android.app.Application
import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManager
import com.ridill.xpensetracker.feature_dashboard.data.preferences.DashboardPreferencesManagerImpl
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.data.repository.ExpenseRepositoryImpl
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.ridill.xpensetracker.feature_expenses.domain.use_case.AddEditUseCases
import com.ridill.xpensetracker.feature_expenses.domain.use_case.DeleteExpenseUseCase
import com.ridill.xpensetracker.feature_expenses.domain.use_case.GetExpenseByIdUseCase
import com.ridill.xpensetracker.feature_expenses.domain.use_case.SaveExpenseUseCase
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

    // Expense Preferences Manager
    @Singleton
    @Provides
    fun provideExpensePreferenceManager(application: Application): DashboardPreferencesManager =
        DashboardPreferencesManagerImpl(application)

    // Expense Repository
    @Singleton
    @Provides
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository = ExpenseRepositoryImpl(dao)

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