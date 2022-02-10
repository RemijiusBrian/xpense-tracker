package com.ridill.xpensetracker.feature_settings.presentation.settings

import androidx.lifecycle.*
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.ridill.xpensetracker.feature_settings.domain.use_case.SettingsUseCases
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: SettingsUseCases,
) : ViewModel(), SettingsActions {

    private val showAppThemeDialog =
        savedStateHandle.getLiveData("showAppThemeDialog", false)

    // Ui State
    val state = combineTuple(
        useCases.getAppPreference(),
        showAppThemeDialog.asFlow(),
    ).map { (
                appPreferences,
                showAppThemeDialog,
            ) ->
        SettingsState(
            currentAppTheme = appPreferences.theme,
            showAppThemeDialog = showAppThemeDialog,
            cashFlowIncludedInExpenditure = appPreferences.cashFlowIncludedInExpenditure
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
            useCases.updateAppTheme(theme)
            showAppThemeDialog.value = false
        }
    }

    override fun onIncludeCashFlowInExpenditureCheckedChange(isChecked: Boolean) {
        viewModelScope.launch {
            useCases.updateCashFlowIncludedInExpenditure(isChecked)
        }
    }
}