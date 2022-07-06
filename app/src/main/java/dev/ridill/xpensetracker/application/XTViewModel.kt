package dev.ridill.xpensetracker.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import javax.inject.Inject

@HiltViewModel
class XTViewModel @Inject constructor(
    preferencesManager: XTPreferencesManager
) : ViewModel() {
    val preferences = preferencesManager.preferences.asLiveData()
    val appTheme = preferences.map { it.theme }
}