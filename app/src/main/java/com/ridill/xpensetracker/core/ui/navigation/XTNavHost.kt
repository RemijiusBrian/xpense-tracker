package com.ridill.xpensetracker.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpensesListScreen

@Composable
fun XTNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Expenses.route,
        modifier = modifier
    ) {
        // Expenses
        composable(route = Destination.Expenses.route) {
            ExpensesListScreen(navController = navController)
        }
    }
}