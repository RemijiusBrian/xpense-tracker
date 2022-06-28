package dev.ridill.xpensetracker.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.ridill.xpensetracker.core.ui.navigation.XTNavHost
import dev.ridill.xpensetracker.core.ui.theme.XpenseTrackerTheme

@AndroidEntryPoint
class XTActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent { ScreenContent() }
    }
}

@Composable
private fun ScreenContent() {
    XpenseTrackerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            XTNavHost(navController = rememberNavController())
        }
    }
}