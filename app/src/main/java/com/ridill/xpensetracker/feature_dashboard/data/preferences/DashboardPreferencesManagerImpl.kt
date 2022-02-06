package com.ridill.xpensetracker.feature_dashboard.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ridill.xpensetracker.feature_dashboard.domain.model.DashboardPreferences
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory
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
            val category = ExpenditureCategory.valueOf(
                preferences[Keys.SELECTED_CATEGORY] ?: ExpenditureCategory.EXPENDITURE.name
            )

            DashboardPreferences(
                expenditureLimit = expenditureLimit,
                category = category,
            )
        }

    override suspend fun updateExpenditureLimit(limit: Long) {
        context.dashboardDatastore.edit { preferences ->
            preferences[Keys.EXPENDITURE_LIMIT] = limit
        }
    }

    override suspend fun updateExpenseCategory(category: ExpenditureCategory) {
        context.dashboardDatastore.edit { preferences ->
            preferences[Keys.SELECTED_CATEGORY] = category.name
        }
    }

    private object Keys {
        val EXPENDITURE_LIMIT = longPreferencesKey("EXPENDITURE_LIMIT")
        val SELECTED_CATEGORY = stringPreferencesKey("SELECTED_CATEGORY")
    }
}