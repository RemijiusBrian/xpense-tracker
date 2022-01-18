package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpenseStore
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpensePreferences
import kotlinx.coroutines.flow.Flow

class GetExpensePreferenceUseCase(
    private val expenseStore: ExpenseStore
) {
    operator fun invoke(): Flow<ExpensePreferences> = expenseStore.preferences
}