package dev.ridill.xpensetracker.core.ui.navigation.screen_specs

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface BottomBarScreenSpec : ScreenSpec {

    companion object {
        val screens: List<BottomBarScreenSpec> = ScreenSpec
            .allScreens
            .values
            .filterIsInstance<BottomBarScreenSpec>()
    }

    val icon: ImageVector
}