package com.ridill.xpensetracker.core.ui

import androidx.lifecycle.ViewModel
import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appPreferenceManager: AppPreferencesManager
) : ViewModel() {

    // App Theme
    val appTheme = appPreferenceManager.preferences.map { it.theme }
}