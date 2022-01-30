package com.ridill.xpensetracker.feature_settings.presentation.settings

import androidx.lifecycle.*
import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManager
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appPreferencesManager: AppPreferencesManager
) : ViewModel(), SettingsActions {

    private val showAppThemeDialog =
        savedStateHandle.getLiveData<Boolean>("showAppThemeDialog", false)

    // Ui State
    val state = combineTuple(
        showAppThemeDialog.asFlow(),
        appPreferencesManager.preferences
    ).map { (
                showAppThemeDialog,
                appPreferences
            ) ->
        SettingsState(
            showAppThemeDialog = showAppThemeDialog,
            currentAppTheme = appPreferences.theme
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
}