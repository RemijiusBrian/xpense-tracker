package com.ridill.xpensetracker.core.ui

import androidx.lifecycle.ViewModel
import com.ridill.xpensetracker.core.data.preferences.AppDatastore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appDatastore: AppDatastore
) : ViewModel() {

    val appTheme = appDatastore.preferences.map { it.theme }
}