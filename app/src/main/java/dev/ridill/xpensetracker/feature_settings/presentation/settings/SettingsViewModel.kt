package dev.ridill.xpensetracker.feature_settings.presentation.settings

import androidx.lifecycle.*
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import dev.ridill.xpensetracker.core.domain.model.AppTheme
import dev.ridill.xpensetracker.core.domain.model.UiText
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.SETTINGS_ACTION_EXPENDITURE_LIMIT_UPDATE
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.SettingsScreenSpec
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.util.exhaustive
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val preferencesManager: XTPreferencesManager
) : ViewModel(), SettingsActions {

    private val preferences = preferencesManager.preferences

    private val showThemeSelection = savedStateHandle.getLiveData("showThemeSelection", false)
    private val showExpenditureLimitUpdate =
        savedStateHandle.getLiveData("showExpenditureLimitUpdate", false)
    private val showBalanceWarningPercentPicker =
        savedStateHandle.getLiveData("showBalanceWarningPercentPicker", false)

    private val eventsChannel = Channel<SettingsEvent>()
    val events get() = eventsChannel.receiveAsFlow()

    init {
        checkSettingsAction()
    }

    val state = combineTuple(
        preferences,
        showThemeSelection.asFlow(),
        showExpenditureLimitUpdate.asFlow(),
        showBalanceWarningPercentPicker.asFlow()
    ).map { (
                preferences,
                showThemeSelection,
                showExpenditureUpdate,
                showBalanceWarningPercentPicker
            ) ->
        SettingsState(
            appTheme = preferences.theme,
            useDynamicTheme = preferences.useDynamicTheming,
            expenditureLimit = TextUtil.formatAmountWithCurrency(preferences.expenditureLimit),
            showThemeSelection = showThemeSelection,
            showExpenditureUpdate = showExpenditureUpdate,
            showBalanceWarningPercentPicker = showBalanceWarningPercentPicker,
            balanceWarningPercent = preferences.balanceWarningPercent
        )
    }.asLiveData()

    private fun checkSettingsAction() {
        SettingsScreenSpec.getActionFromSavedStateHandle(savedStateHandle)
            ?.let { action ->
                when (action) {
                    SETTINGS_ACTION_EXPENDITURE_LIMIT_UPDATE -> {
                        showExpenditureLimitUpdate.value = true
                    }
                    else -> Unit
                }.exhaustive
            }
    }

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
        showExpenditureLimitUpdate.value = true
    }

    override fun onExpenditureLimitUpdateDismiss() {
        showExpenditureLimitUpdate.value = false
    }

    override fun onExpenditureLimitUpdateConfirm(amount: String) {
        val parsedAmount = amount.toLongOrNull() ?: return
        viewModelScope.launch {
            if (parsedAmount < 0L) {
                eventsChannel.send(SettingsEvent.ShowErrorMessage(UiText.StringResource(R.string.error_invalid_amount)))
                return@launch
            }
            preferencesManager.updateExpenditureLimit(parsedAmount)
            eventsChannel.send(SettingsEvent.ShowUiMessage(UiText.StringResource(R.string.expenditure_limit_updated)))
            showExpenditureLimitUpdate.value = false
        }
    }

    override fun onShowLowBalanceUnderPercentPreferenceClick() {
        showBalanceWarningPercentPicker.value = true
    }

    override fun onShowLowBalanceUnderPercentUpdateDismiss() {
        showBalanceWarningPercentPicker.value = false
    }

    override fun onShowLowBalanceUnderPercentUpdateConfirm(value: Float) {
        viewModelScope.launch {
            preferencesManager.updateBalanceWarningPercent(value)
            showBalanceWarningPercentPicker.value = false
            eventsChannel.send(SettingsEvent.ShowUiMessage(UiText.StringResource(R.string.value_updated)))
        }
    }

    sealed class SettingsEvent {
        data class ShowErrorMessage(val message: UiText) : SettingsEvent()
        data class ShowUiMessage(val message: UiText) : SettingsEvent()
    }
}