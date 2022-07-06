package dev.ridill.xpensetracker.feature_settings.presentation.settings

import androidx.lifecycle.*
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import dev.ridill.xpensetracker.core.domain.model.AppTheme
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.util.toLongOrZero
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferencesManager: XTPreferencesManager
) : ViewModel(), SettingsActions {

    private val preferences = preferencesManager.preferences

    private val showThemeSelection = savedStateHandle.getLiveData("showThemeSelection", false)
    private val showExpenditureUpdate = savedStateHandle.getLiveData("showExpenditureUpdate", false)

    val state = combineTuple(
        preferences,
        showThemeSelection.asFlow(),
        showExpenditureUpdate.asFlow()
    ).map { (
                preferences,
                showThemeSelection,
                showExpenditureUpdate
            ) ->
        SettingsState(
            appTheme = preferences.theme,
            useDynamicTheme = preferences.useDynamicTheming,
            expenditureLimit = TextUtil.formatAmountWithCurrency(preferences.expenditureLimit),
            showThemeSelection = showThemeSelection,
            showExpenditureUpdate = showExpenditureUpdate
        )
    }.asLiveData()

    override fun onThemePreferenceClick() {
        showThemeSelection.value = true
    }

    override fun onAppThemeSelectionDismiss() {
        showThemeSelection.value = false
    }

    override fun onAppThemeSelectionConfirm(theme: AppTheme) {
        viewModelScope.launch {
            preferencesManager.updateAppTheme(theme)
            showThemeSelection.value = false
        }
    }

    override fun onUseDynamicCheckedChange(isChecked: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateUseDynamicTheming(isChecked)
        }
    }

    override fun onExpenditureLimitPreferenceClick() {
        showExpenditureUpdate.value = true
    }

    override fun onExpenditureLimitUpdateDismiss() {
        showExpenditureUpdate.value = false
    }

    override fun onExpenditureLimitUpdateConfirm(amount: String) {
        viewModelScope.launch {
            val parsedAmount = amount.toLongOrZero()
            if (parsedAmount < 0L) {
                return@launch
            }
            preferencesManager.updateExpenditureLimit(parsedAmount)
            showExpenditureUpdate.value = false
        }
    }
}