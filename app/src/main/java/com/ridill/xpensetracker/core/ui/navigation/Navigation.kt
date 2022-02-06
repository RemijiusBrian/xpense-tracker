package com.ridill.xpensetracker.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.ridill.xpensetracker.core.ui.util.slideInHorizontallyWithFadeIn
import com.ridill.xpensetracker.core.ui.util.slideOutHorizontallyWithFadeOut
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.ui.CashFlowDetails
import com.ridill.xpensetracker.feature_dashboard.presentation.dashboard.ui.Dashboard
import com.ridill.xpensetracker.feature_expenditures.presentation.add_edit_expenditure.ui.AddEditExpenditure
import com.ridill.xpensetracker.feature_settings.presentation.settings.ui.Settings

@Composable
fun Navigation(
    navController: NavHostController,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Destination.Dashboard.route,
    ) {
        composable(
            route = Destination.Dashboard.route,
            exitTransition = { slideOutHorizontallyWithFadeOut(false) },
            popEnterTransition = { slideInHorizontallyWithFadeIn(false) }
        ) {
            Dashboard(navController = navController)
        }
        composable(
            route = Destination.AddEditExpense.route + "?${NavArgs.EXPENSE_ID}={${NavArgs.EXPENSE_ID}}",
            arguments = Destination.AddEditExpense.arguments,
            enterTransition = { slideInHorizontallyWithFadeIn() },
            popExitTransition = { slideOutHorizontallyWithFadeOut() }
        ) {
            AddEditExpenditure(navController = navController)
        }

        composable(
            route = Destination.CashFlowDetails.route + "?${NavArgs.CASH_FLOW_EXPENSE}={${NavArgs.CASH_FLOW_EXPENSE}}",
            arguments = Destination.CashFlowDetails.arguments,
            enterTransition = { slideInHorizontallyWithFadeIn() },
            popExitTransition = { slideOutHorizontallyWithFadeOut() }
        ) {
            CashFlowDetails(navController = navController)
        }

        composable(
            route = Destination.Settings.route,
            enterTransition = { slideInHorizontallyWithFadeIn() },
            popExitTransition = { slideOutHorizontallyWithFadeOut() }
        ) {
            Settings(navController = navController)
        }
    }
}