package com.ridill.xpensetracker.core.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.ridill.xpensetracker.core.ui.util.slideInHorizontallyWithFadeIn
import com.ridill.xpensetracker.core.ui.util.slideOutHorizontallyWithFadeOut
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents.ui.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.ui.CashFlowDetails
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ui.AddEditExpense
import com.ridill.xpensetracker.feature_expenses.presentation.expense_tags.ExpenseTags
import com.ridill.xpensetracker.feature_expenses.presentation.expenses.ui.Expenses
import com.ridill.xpensetracker.feature_settings.presentation.settings.ui.Settings

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Destination.Expenses.route,
        modifier = modifier
    ) {
        // Expenses
        composable(
            route = Destination.Expenses.route,
            enterTransition = { fadeIn(animationSpec = tween()) },
            exitTransition = { fadeOut(animationSpec = tween()) },
            popEnterTransition = { fadeIn(animationSpec = tween()) },
            popExitTransition = { fadeOut(animationSpec = tween()) },
        ) {
            Expenses(navController = navController)
        }

        // Cash Flow
        composable(
            route = Destination.CashFlow.route,
            enterTransition = { fadeIn(animationSpec = tween()) },
            exitTransition = { fadeOut(animationSpec = tween()) },
            popEnterTransition = { fadeIn(animationSpec = tween()) },
            popExitTransition = { fadeOut(animationSpec = tween()) },
        ) {
            CashFlow(navController = navController)
        }

        // Settings
        composable(
            route = Destination.Settings.route,
            enterTransition = { fadeIn(animationSpec = tween()) },
            exitTransition = { fadeOut(animationSpec = tween()) },
            popEnterTransition = { fadeIn(animationSpec = tween()) },
            popExitTransition = { fadeOut(animationSpec = tween()) },
        ) {
            Settings()
        }

        // Add/Edit Expense
        composable(
            route = Destination.AddEditExpense.route + "?${NavArgs.EXPENSE_ID}={${NavArgs.EXPENSE_ID}}",
            arguments = Destination.AddEditExpense.arguments,
            enterTransition = { slideInHorizontallyWithFadeIn() },
            popExitTransition = { slideOutHorizontallyWithFadeOut() }
        ) {
            AddEditExpense(navController = navController)
        }

        // Cash Flow Details
        composable(
            route = Destination.CashFlowDetails.route + "?${NavArgs.AGENT_ID}={${NavArgs.AGENT_ID}}",
            arguments = Destination.CashFlowDetails.arguments,
            enterTransition = { slideInHorizontallyWithFadeIn() },
            popExitTransition = { slideOutHorizontallyWithFadeOut() }
        ) {
            CashFlowDetails(navController = navController)
        }

        // Expense Tags
        composable(
            route = Destination.ExpenseTags.route,
            enterTransition = { fadeIn(animationSpec = tween()) },
            exitTransition = { fadeOut(animationSpec = tween()) },
            popEnterTransition = { fadeIn(animationSpec = tween()) },
            popExitTransition = { fadeOut(animationSpec = tween()) },
        ) {
            ExpenseTags(navController = navController)
        }
    }
}