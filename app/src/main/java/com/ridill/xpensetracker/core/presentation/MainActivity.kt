package com.ridill.xpensetracker.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.ridill.xpensetracker.core.presentation.navigation.Navigation
import com.ridill.xpensetracker.core.presentation.ui.theme.XpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val appTheme by viewModel.appTheme.collectAsState(initial = AppTheme.SYSTEM_DEFAULT)

            XpenseTrackerTheme(
                darkTheme = when (appTheme) {
                    AppTheme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true
                }
            ) {
                ScreenContent()
            }
        }
    }
}

@Composable
private fun ScreenContent() {
    val navController = rememberAnimatedNavController()

    Surface(color = MaterialTheme.colors.background) {
        Navigation(navController)
    }
}

const val ARG_HIDE_BOTTOM_BAR = "ARG_HIDE_BOTTOM_BAR"