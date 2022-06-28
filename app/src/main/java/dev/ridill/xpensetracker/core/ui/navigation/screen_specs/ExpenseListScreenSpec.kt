package dev.ridill.xpensetracker.core.ui.navigation.screen_specs

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpensesListScreen

object ExpenseListScreenSpec : ScreenSpec {

    override val label: Int = R.string.destination_expenses

    override val navHostRoute: String = "expenseList"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        ExpensesListScreen(navController = navController)
    }
}