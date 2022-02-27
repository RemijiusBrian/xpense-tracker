package com.ridill.xpensetracker.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.ridill.xpensetracker.core.ui.navigation.ARG_HIDE_BOTTOM_BAR
import com.ridill.xpensetracker.core.ui.navigation.BottomNavDestinations
import com.ridill.xpensetracker.core.ui.navigation.Navigation
import com.ridill.xpensetracker.core.ui.theme.XpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val appTheme by viewModel.appTheme.collectAsState(initial = AppTheme.SYSTEM_DEFAULT)
            val darkTheme = when (appTheme) {
                AppTheme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
            }
            XpenseTrackerTheme(darkTheme) {
                ScreenContent()
            }
        }
    }
}

@Composable
private fun ScreenContent() {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            val hideBottomBar = navBackStackEntry?.arguments?.getBoolean(ARG_HIDE_BOTTOM_BAR)

            AnimatedVisibility(
                visible = hideBottomBar == null || !hideBottomBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BottomNavigation {
                    val currentDestination = navBackStackEntry?.destination

                    Row(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        BottomNavDestinations.forEach { destination ->
                            BottomNavigationItem(
                                selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                                onClick = {
                                    if (currentDestination?.route == destination.route) return@BottomNavigationItem
                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    destination.icon?.let {
                                        Icon(
                                            painter = painterResource(it),
                                            contentDescription = stringResource(destination.label)
                                        )
                                    }
                                },
                                label = { Text(stringResource(destination.label)) },
                                alwaysShowLabel = false
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Navigation(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController
        )
    }
}