package com.ridill.xpensetracker.feature_settings.presentation.settings

import androidx.lifecycle.*
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferencesManager: XTPreferencesManager,
) : ViewModel(), SettingsActions {

    private val showAppThemeDialog =
        savedStateHandle.getLiveData("showAppThemeDialog", false)

    // Preferences
    private val preferences = preferencesManager.preferences

    // Ui State
    val state = combineTuple(
        preferences,
        showAppThemeDialog.asFlow(),
    ).map { (
                preferences,
                showAppThemeDialog,
            ) ->
        SettingsState(
            currentAppTheme = preferences.theme,
            showAppThemeDialog = showAppThemeDialog,
            cashFlowIncludedInExpenditure = preferences.cashFlowIncludedInExpenditure
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
            preferencesManager.updateAppTheme(theme)
            showAppThemeDialog.value = false
        }
    }

    override fun onIncludeCashFlowInExpenditureCheckedChange(isChecked: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateCashFlowIncludedInExpenditure(isChecked)
        }
    }
}