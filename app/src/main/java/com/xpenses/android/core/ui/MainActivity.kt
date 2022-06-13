package com.xpenses.android.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.xpenses.android.core.ui.navigation.XTNavHost
import com.xpenses.android.core.ui.theme.XpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            XpenseTrackerTheme {
                ScreenContent()
            }
        }
    }
}

@Composable
private fun ScreenContent() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        XTNavHost(navController = rememberNavController())
    }
}