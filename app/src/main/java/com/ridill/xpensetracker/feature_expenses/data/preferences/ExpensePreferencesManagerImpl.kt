package com.ridill.xpensetracker.feature_expenses.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpensePreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.expenseDatastore by preferencesDataStore(ExpensePreferencesManagerImpl.NAME)

class ExpensePreferencesManagerImpl(
    private val context: Context
) : ExpensePreferencesManager {

    companion object {
        const val NAME = "expense_datastore"
    }

    override val preferences = context.expenseDatastore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            val expenditureLimit = preferences[Keys.EXPENDITURE_LIMIT] ?: 0L

            ExpensePreferences(
                expenditureLimit = expenditureLimit,
            )
        }

    override suspend fun updateExpenditureLimit(limit: Long) {
        context.expenseDatastore.edit { preferences ->
            preferences[Keys.EXPENDITURE_LIMIT] = limit
        }
    }

    private object Keys {
        val EXPENDITURE_LIMIT = longPreferencesKey("EXPENDITURE_LIMIT")
    }
}