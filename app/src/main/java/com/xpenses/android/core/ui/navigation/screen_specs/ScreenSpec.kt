package com.xpenses.android.core.ui.navigation.screen_specs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink

sealed interface ScreenSpec {
    companion object {
        val allScreens = listOf<ScreenSpec>(
            ExpenseListScreenSpec,
            AddEditExpenseScreenSpec,
            BillsListScreenSpec,
            AddBillScreenSpec
        ).associateBy { it.navHostRoute }
    }

    @get:StringRes
    val label: Int

    val navHostRoute: String

    val arguments: List<NamedNavArgument> get() = emptyList()

    val deepLinks: List<NavDeepLink> get() = emptyList()

    @Composable
    fun Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    )
}