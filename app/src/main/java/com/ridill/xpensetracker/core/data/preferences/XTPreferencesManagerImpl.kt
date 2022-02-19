package com.ridill.xpensetracker.core.data.preferences

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.ridill.xpensetracker.core.domain.model.AppPreferences
import com.ridill.xpensetracker.core.domain.model.AppTheme
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class XTPreferencesManagerImpl(
    private val dataStore: DataStore<Preferences>
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
            val isFirstAppLaunch = preferences[Keys.IS_FIRST_APP_LAUNCH] ?: true

            AppPreferences(
                theme = theme,
                expenditureLimit = expenditureLimit,
                cashFlowIncludedInExpenditure = cashFlowIncludedInExpenditure,
                isFirstAppLaunch = isFirstAppLaunch
            )
        }

    override suspend fun updateAppTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[Keys.APP_THEME] = theme.name
        }
    }

    override suspend fun updateExpenditureLimit(limit: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.EXPENDITURE_LIMIT] = limit
        }
    }

    override suspend fun updateCashFlowIncludedInExpenditure(include: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.CASH_FLOW_INCLUDED_IN_EXPENDITURE] = include
        }
    }

    override suspend fun toggleIsFirstLaunchFalse() {
        dataStore.edit { preferences ->
            preferences[Keys.IS_FIRST_APP_LAUNCH] = false
        }
    }

    private object Keys {
        val APP_THEME = stringPreferencesKey("APP_THEME")
        val EXPENDITURE_LIMIT = longPreferencesKey("EXPENDITURE_LIMIT")
        val CASH_FLOW_INCLUDED_IN_EXPENDITURE =
            booleanPreferencesKey("CASH_FLOW_INCLUDED_IN_EXPENDITURE")
        val IS_FIRST_APP_LAUNCH = booleanPreferencesKey("IS_FIRST_APP_LAUNCH")
    }
}

class XTPreferencesMigration : DataMigration<Preferences> {
    override suspend fun cleanUp() {

    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        return currentData
    }

    override suspend fun shouldMigrate(currentData: Preferences): Boolean = true
}