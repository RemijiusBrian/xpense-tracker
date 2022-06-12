package com.ridill.xpensetracker.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.AddEditExpenseScreen
import com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpensesListScreen

@Composable
fun XTNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = XTDestination.Expenses.route,
        modifier = modifier
    ) {
        // Expenses
        composable(route = XTDestination.Expenses.route) {
            ExpensesListScreen(navController = navController)
        }

        // Add/Edit Expense
        composable(
            route = XTDestination.AddEditExpense.navRoute,
            arguments = XTDestination.AddEditExpense.arguments
        ) {
            AddEditExpenseScreen(navController = navController)
        }
    }
}