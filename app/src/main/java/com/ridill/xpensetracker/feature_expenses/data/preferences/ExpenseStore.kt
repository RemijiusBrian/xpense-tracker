package com.ridill.xpensetracker.feature_expenses.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpensePreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.expenseDatastore by preferencesDataStore(ExpenseStore.NAME)

class ExpenseStore(
    private val context: Context
) {
    companion object {
        const val NAME = "expense_datastore"
    }

    val preferences = context.expenseDatastore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            val expenditureLimit = preferences[Keys.EXPENDITURE_LIMIT] ?: 0L
            val category = ExpenseCategory.valueOf(
                preferences[Keys.CATEGORY] ?: ExpenseCategory.EXPENSE.name
            )
            val showAll = preferences[Keys.SHOW_ALL_ENTRIES] ?: false

            ExpensePreferences(
                expenditureLimit = expenditureLimit,
                category = category,
                showAllEntries = showAll
            )
        }

    suspend fun updateExpenditureLimit(limit: Long) {
        context.expenseDatastore.edit { preferences ->
            preferences[Keys.EXPENDITURE_LIMIT] = limit
        }
    }

    suspend fun updateExpenseCategory(category: ExpenseCategory) {
        context.expenseDatastore.edit { preferences ->
            preferences[Keys.CATEGORY] = category.name
        }
    }

    suspend fun updateShowPreviousEntries(show: Boolean) {
        context.expenseDatastore.edit { preferences ->
            preferences[Keys.SHOW_ALL_ENTRIES] = show
        }
    }

    private object Keys {
        val EXPENDITURE_LIMIT = longPreferencesKey("expenditureLimit")
        val CATEGORY = stringPreferencesKey("expenseCategory")
        val SHOW_ALL_ENTRIES = booleanPreferencesKey("showAllEntries")
    }
}