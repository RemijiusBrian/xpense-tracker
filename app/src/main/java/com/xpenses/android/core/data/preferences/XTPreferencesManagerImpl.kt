package com.xpenses.android.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.xpenses.android.core.domain.model.XTPreferences
import com.xpenses.android.core.util.DispatcherProvider
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

            XTPreferences(
                expenditureLimit = expenditureLimit
            )
        }

    override suspend fun updateExpenditureLimit(limit: Long) {
        withContext(dispatcherProvider.io) {
            dataStore.edit { preferences ->
                preferences[Keys.EXPENDITURE_LIMIT] = limit
            }
        }
    }

    private object Keys {
        val EXPENDITURE_LIMIT = longPreferencesKey("EXPENDITURE_LIMIT")
    }
}