package com.ridill.xpensetracker.core.ui

import androidx.lifecycle.ViewModel
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    preferencesManager: XTPreferencesManager
) : ViewModel() {

    // App Theme
    val appTheme = preferencesManager.preferences.map { it.theme }
}