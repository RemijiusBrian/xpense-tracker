package com.ridill.xpensetracker.feature_settings.presentation.settings

import androidx.lifecycle.*
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.ridill.xpensetracker.core.ui.util.TextUtil
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

    private val showExpenditureLimitDialog =
        savedStateHandle.getLiveData("showExpenditureLimitDialog", false)

    // Ui State
    val state = combineTuple(
        useCases.getAppPreference(),
        showAppThemeDialog.asFlow(),
        useCases.getDashboardPreferences(),
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
            useCases.updateAppTheme(theme)
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
            useCases.updateExpenditureLimitUseCase(limit)
            showExpenditureLimitDialog.value = false
        }
    }
}