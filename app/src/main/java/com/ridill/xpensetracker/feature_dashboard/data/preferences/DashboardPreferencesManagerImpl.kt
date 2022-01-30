package com.ridill.xpensetracker.feature_dashboard.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ridill.xpensetracker.feature_dashboard.domain.model.DashboardPreferences
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dashboardDatastore by preferencesDataStore(DashboardPreferencesManagerImpl.NAME)

class DashboardPreferencesManagerImpl(
    private val context: Context
) : DashboardPreferencesManager {

    companion object {
        const val NAME = "dashboard_datastore"
    }

    override val preferences = context.dashboardDatastore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            val expenditureLimit = preferences[Keys.EXPENDITURE_LIMIT] ?: 0L
            val category = ExpenseCategory.valueOf(
                preferences[Keys.SELECTED_CATEGORY] ?: ExpenseCategory.EXPENSE.name
            )
            val showAll = preferences[Keys.SHOW_ALL_ENTRIES] ?: false

            DashboardPreferences(
                expenditureLimit = expenditureLimit,
                category = category,
                showAllEntries = showAll
            )
        }

    override suspend fun updateExpenditureLimit(limit: Long) {
        context.dashboardDatastore.edit { preferences ->
            preferences[Keys.EXPENDITURE_LIMIT] = limit
        }
    }

    override suspend fun updateExpenseCategory(category: ExpenseCategory) {
        context.dashboardDatastore.edit { preferences ->
            preferences[Keys.SELECTED_CATEGORY] = category.name
        }
    }

    override suspend fun updateShowPreviousEntries(show: Boolean) {
        context.dashboardDatastore.edit { preferences ->
            preferences[Keys.SHOW_ALL_ENTRIES] = show
        }
    }

    private object Keys {
        val EXPENDITURE_LIMIT = longPreferencesKey("EXPENDITURE_LIMIT")
        val SELECTED_CATEGORY = stringPreferencesKey("SELECTED_CATEGORY")
        val SHOW_ALL_ENTRIES = booleanPreferencesKey("SHOW_ALL_ENTRIES")
    }
}