package com.ridill.xpensetracker.feature_expenses.domain.use_case

import com.ridill.xpensetracker.feature_expenses.data.preferences.ExpensePreferencesManager
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpensePreferences
import kotlinx.coroutines.flow.Flow

class GetExpensePreferencesUseCase(
    private val preferenceManager: ExpensePreferencesManager
) {
    operator fun invoke(): Flow<ExpensePreferences> = preferenceManager.preferences
}