package dev.ridill.xpensetracker.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.ScreenSpec

@Composable
fun XTNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = ScreenSpec.allScreens.values.first().navHostRoute,
        modifier = modifier
    ) {
        ScreenSpec.allScreens.values.forEach { screenSpec ->
            composable(
                route = screenSpec.navHostRoute,
                arguments = screenSpec.arguments,
                deepLinks = screenSpec.deepLinks
            ) { navBackStackEntry ->
                screenSpec.Content(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry
                )
            }
        }
    }
}