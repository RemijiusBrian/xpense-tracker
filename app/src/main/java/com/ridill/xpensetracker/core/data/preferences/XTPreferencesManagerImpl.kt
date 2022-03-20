package com.ridill.xpensetracker.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.ridill.xpensetracker.core.domain.model.AppPreferences
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.ridill.xpensetracker.core.util.DispatcherProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class XTPreferencesManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider
) : XTPreferencesManager {

    override val preferences = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            val theme =
                AppTheme.valueOf(preferences[Keys.APP_THEME] ?: AppTheme.SYSTEM_DEFAULT.name)
            val expenditureLimit = preferences[Keys.EXPENDITURE_LIMIT] ?: 0L
            val cashFlowIncludedInExpenditure =
                preferences[Keys.CASH_FLOW_INCLUDED_IN_EXPENDITURE] ?: false

            AppPreferences(
                theme = theme,
                expenditureLimit = expenditureLimit,
                cashFlowIncludedInExpenditure = cashFlowIncludedInExpenditure,
            )
        }

    override suspend fun updateAppTheme(theme: AppTheme) {
        withContext(dispatcherProvider.io) {
            dataStore.edit { preferences ->
                preferences[Keys.APP_THEME] = theme.name
            }
        }
    }

    override suspend fun updateExpenditureLimit(limit: Long) {
        withContext(dispatcherProvider.io) {
            dataStore.edit { preferences ->
                preferences[Keys.EXPENDITURE_LIMIT] = limit
            }
        }
    }

    override suspend fun updateCashFlowIncludedInExpenditure(include: Boolean) {
        withContext(dispatcherProvider.io) {
            dataStore.edit { preferences ->
                preferences[Keys.CASH_FLOW_INCLUDED_IN_EXPENDITURE] = include
            }
        }
    }

    private object Keys {
        val APP_THEME = stringPreferencesKey("APP_THEME")
        val EXPENDITURE_LIMIT = longPreferencesKey("EXPENDITURE_LIMIT")
        val CASH_FLOW_INCLUDED_IN_EXPENDITURE =
            booleanPreferencesKey("CASH_FLOW_INCLUDED_IN_EXPENDITURE")
    }
}