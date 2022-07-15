package dev.ridill.xpensetracker.feature_settings.presentation.settings

import androidx.lifecycle.*
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import dev.ridill.xpensetracker.core.domain.model.AppTheme
import dev.ridill.xpensetracker.core.domain.model.UiText
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.util.toLongOrZero
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
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
    private val showLowBalanceWarningUnderPercentPicker =
        savedStateHandle.getLiveData("showLowBalanceWarningUnderPercentPicker", false)

    private val eventsChannel = Channel<SettingsEvent>()
    val events get() = eventsChannel.receiveAsFlow()

    val state = combineTuple(
        preferences,
        showThemeSelection.asFlow(),
        showExpenditureUpdate.asFlow(),
        showLowBalanceWarningUnderPercentPicker.asFlow()
    ).map { (
                preferences,
                showThemeSelection,
                showExpenditureUpdate,
                showLowBalanceWarningUnderPercentPicker
            ) ->
        SettingsState(
            appTheme = preferences.theme,
            useDynamicTheme = preferences.useDynamicTheming,
            expenditureLimit = TextUtil.formatAmountWithCurrency(preferences.expenditureLimit),
            showThemeSelection = showThemeSelection,
            showExpenditureUpdate = showExpenditureUpdate,
            showWarningUnderBalancePercentPicker = showLowBalanceWarningUnderPercentPicker,
            showWarningUnderBalancePercent = preferences.showWarningUnderBalancePercent
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
                eventsChannel.send(SettingsEvent.ShowErrorMessage(UiText.StringResource(R.string.error_invalid_amount)))
                return@launch
            }
            preferencesManager.updateExpenditureLimit(parsedAmount)
            eventsChannel.send(SettingsEvent.ShowUiMessage(UiText.StringResource(R.string.expenditure_limit_updated)))
            showExpenditureUpdate.value = false
        }
    }

    override fun onShowLowBalanceUnderPercentPreferenceClick() {
        showLowBalanceWarningUnderPercentPicker.value = true
    }

    override fun onShowLowBalanceUnderPercentUpdateDismiss() {
        showLowBalanceWarningUnderPercentPicker.value = false
    }

    override fun onShowLowBalanceUnderPercentUpdateConfirm(value: Float) {
        viewModelScope.launch {
            preferencesManager.updateShowBalanceWarningBelowPercent(value)
            showLowBalanceWarningUnderPercentPicker.value = false
            eventsChannel.send(SettingsEvent.ShowUiMessage(UiText.StringResource(R.string.value_updated)))
        }
    }

    sealed class SettingsEvent {
        data class ShowErrorMessage(val message: UiText) : SettingsEvent()
        data class ShowUiMessage(val message: UiText) : SettingsEvent()
    }
}