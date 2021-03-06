package dev.ridill.xpensetracker.core.data.preferences

import dev.ridill.xpensetracker.core.domain.model.XTPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePreferencesManager : XTPreferencesManager {

    private var preferencesObj = XTPreferences(1000L)
    private val preferencesFlow = flowOf(preferencesObj)

    override val preferences: Flow<XTPreferences>
        get() = preferencesFlow

    override suspend fun updateExpenditureLimit(limit: Long) {
        preferencesObj = preferencesObj.copy(limit)
    }
}