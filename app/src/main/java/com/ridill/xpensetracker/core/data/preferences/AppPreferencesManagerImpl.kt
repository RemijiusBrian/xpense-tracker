package com.ridill.xpensetracker.core.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ridill.xpensetracker.core.domain.model.AppPreferences
import com.ridill.xpensetracker.core.domain.model.AppTheme
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.appDatastore by preferencesDataStore(AppPreferencesManagerImpl.NAME)

class AppPreferencesManagerImpl(
    private val context: Context
) : AppPreferencesManager {
    companion object {
        const val NAME = "app_datastore"
    }

    override val preferences = context.appDatastore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            val theme =
                AppTheme.valueOf(preferences[Keys.APP_THEME] ?: AppTheme.SYSTEM_DEFAULT.name)

            val cashFlowIncludedInExpenditure =
                preferences[Keys.CASH_FLOW_INCLUDED_IN_EXPENDITURE] ?: false

            AppPreferences(
                theme = theme,
                cashFlowIncludedInExpenditure = cashFlowIncludedInExpenditure
            )
        }

    override suspend fun updateAppTheme(theme: AppTheme) {
        context.appDatastore.edit { preferences ->
            preferences[Keys.APP_THEME] = theme.name
        }
    }

    override suspend fun updateCashFlowInlcudedInExpenditure(include: Boolean) {
        context.appDatastore.edit { preferences ->
            preferences[Keys.CASH_FLOW_INCLUDED_IN_EXPENDITURE] = include
        }
    }

    private object Keys {
        val APP_THEME = stringPreferencesKey("APP_THEME")
        val CASH_FLOW_INCLUDED_IN_EXPENDITURE =
            booleanPreferencesKey("CASH_FLOW_INCLUDED_IN_EXPENDITURE")
    }
}