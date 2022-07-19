package dev.ridill.xpensetracker.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import dev.ridill.xpensetracker.core.domain.model.AppTheme
import dev.ridill.xpensetracker.core.ui.navigation.XTNavHost
import dev.ridill.xpensetracker.core.ui.theme.XpenseTrackerTheme

@AndroidEntryPoint
class XTActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val viewModel: XTViewModel = hiltViewModel()
            val preferences by viewModel.preferences.observeAsState()

            @Suppress("NAME_SHADOWING")
            preferences?.let { preferences ->
                val useDynamicTheming = preferences.useDynamicTheming
                val darkTheme = when (preferences.theme) {
                    AppTheme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true
                }
                ScreenContent(
                    darkTheme = darkTheme,
                    useDynamicTheming = useDynamicTheming
                )
            }
        }
    }
}

@Composable
private fun ScreenContent(
    darkTheme: Boolean,
    useDynamicTheming: Boolean
) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.RECEIVE_SMS
        )
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    XpenseTrackerTheme(
        darkTheme = darkTheme,
        useDynamicTheming = useDynamicTheming
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            XTNavHost(navController = rememberNavController())
        }
    }
}