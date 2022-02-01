package com.ridill.xpensetracker.feature_settings.presentation.settings

import androidx.lifecycle.*
import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManager
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.feature_dashboard.data.preferences.ExpensePreferenceManager
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appPreferencesManager: AppPreferencesManager,
    private val expensePreferences: ExpensePreferenceManager
) : ViewModel(), SettingsActions {

    private val showAppThemeDialog =
        savedStateHandle.getLiveData<Boolean>("showAppThemeDialog", false)

    private val showExpenditureLimitDialog =
        savedStateHandle.getLiveData<Boolean>("showExpenditureLimitDialog", false)

    // Ui State
    val state = combineTuple(
        appPreferencesManager.preferences,
        showAppThemeDialog.asFlow(),
        expensePreferences.preferences,
        showExpenditureLimitDialog.asFlow()
    ).map { (
                appPreferences,
                showAppThemeDialog,
                expensePreferences,
                showExpenditureLimitDialog
            ) ->
        SettingsState(
            currentAppTheme = appPreferences.theme,
            showAppThemeDialog = showAppThemeDialog,
            currentExpenditureLimit = "${TextUtil.currencySymbol} ${
                TextUtil.formatNumber(expensePreferences.expenditureLimit)
            }",
            showExpenditureLimitDialog = showExpenditureLimitDialog
        )
    }.asLiveData()

    override fun onAppThemePreferenceClick() {
        showAppThemeDialog.value = true
    }

    override fun onAppThemeDialogDismiss() {
        showAppThemeDialog.value = false
    }

    override fun onAppThemeDialogConfirm(theme: AppTheme) {
        viewModelScope.launch {
            appPreferencesManager.updateAppTheme(theme)
            showAppThemeDialog.value = false
        }
    }

    override fun onExpenditureLimitPreferenceClick() {
        showExpenditureLimitDialog.value = true
    }

    override fun onExpenditureLimitDialogDismiss() {
        showExpenditureLimitDialog.value = false
    }

    override fun onExpenditureLimitDialogConfirm(limit: String) {
        viewModelScope.launch {
            val amount =
                limit.toLongOrNull() ?: expensePreferences.preferences.first().expenditureLimit
            expensePreferences.updateExpenditureLimit(amount)
            showExpenditureLimitDialog.value = false
        }
    }
}