package dev.ridill.xpensetracker.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import dev.ridill.xpensetracker.core.domain.model.AppTheme
import dev.ridill.xpensetracker.core.domain.model.XTPreferences
import dev.ridill.xpensetracker.core.util.DispatcherProvider
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
            val expenditureLimit = preferences[Keys.EXPENDITURE_LIMIT] ?: 0L
            val theme = AppTheme.valueOf(
                preferences[Keys.APP_THEME] ?: AppTheme.SYSTEM_DEFAULT.name
            )
            val useDynamicTheming = preferences[Keys.USE_DYNAMIC_THEMING] ?: true

            XTPreferences(
                expenditureLimit = expenditureLimit,
                theme = theme,
                useDynamicTheming = useDynamicTheming
            )
        }

    override suspend fun updateExpenditureLimit(limit: Long) {
        withContext(dispatcherProvider.io) {
            dataStore.edit { preferences ->
                preferences[Keys.EXPENDITURE_LIMIT] = limit
            }
        }
    }

    override suspend fun updateAppTheme(theme: AppTheme) {
        withContext(dispatcherProvider.io) {
            dataStore.edit { preferences ->
                preferences[Keys.APP_THEME] = theme.name
            }
        }
    }

    override suspend fun updateUseDynamicTheming(use: Boolean) {
        withContext(dispatcherProvider.io) {
            dataStore.edit { preferences ->
                preferences[Keys.USE_DYNAMIC_THEMING] = use
            }
        }
    }

    private object Keys {
        val EXPENDITURE_LIMIT = longPreferencesKey("EXPENDITURE_LIMIT")
        val APP_THEME = stringPreferencesKey("APP_THEME")
        val USE_DYNAMIC_THEMING = booleanPreferencesKey("USE_DYNAMIC_THEMING")
    }
}