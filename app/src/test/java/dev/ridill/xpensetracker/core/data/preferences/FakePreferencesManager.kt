package dev.ridill.xpensetracker.core.data.preferences

import dev.ridill.xpensetracker.core.domain.model.AppTheme
import dev.ridill.xpensetracker.core.domain.model.XTPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakePreferencesManager : XTPreferencesManager {

    private var preferencesObj = MutableStateFlow(
        XTPreferences(
            expenditureLimit = 1000L,
            theme = AppTheme.SYSTEM_DEFAULT,
            useDynamicTheming = true,
            balanceWarningPercent = 0.25f
        )
    )

    private val preferencesFlow = preferencesObj.asStateFlow()

    override val preferences: Flow<XTPreferences>
        get() = preferencesFlow

    override suspend fun updateExpenditureLimit(limit: Long) {
        preferencesObj.update { it.copy(expenditureLimit = limit) }
    }

    override suspend fun updateAppTheme(theme: AppTheme) {
        preferencesObj.update { it.copy(theme = theme) }
    }

    override suspend fun updateUseDynamicTheming(use: Boolean) {
        preferencesObj.update { it.copy(useDynamicTheming = use) }
    }

    override suspend fun updateBalanceWarningPercent(value: Float) {
        preferencesObj.update { it.copy(balanceWarningPercent = value) }
    }
}