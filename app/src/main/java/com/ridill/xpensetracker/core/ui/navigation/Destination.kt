package com.ridill.xpensetracker.core.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ridill.xpensetracker.R

sealed class Destination(
    val route: String,
    @StringRes val label: Int,
    val arguments: List<NamedNavArgument> = emptyList(),
    @DrawableRes val icon: Int? = null,
) {
    object Expenses : Destination(
        route = "expenses",
        label = R.string.expenses,
        icon = R.drawable.ic_expense
    )

    object CashFlow : Destination(
        route = "cashFlow",
        label = R.string.cash_flow,
        icon = R.drawable.ic_cash_flow
    )

    object ExpensePlans : Destination(
        route = "expensePlans",
        label = R.string.expense_plans,
        icon = R.drawable.ic_expense_plans
    )

    object AddEditExpense : Destination(
        route = "addEditExpense",
        label = R.string.add_edit_expense,
        arguments = listOf(
            navArgument(NavArgs.EXPENSE_ID) {
                type = NavType.LongType
                nullable = false
                defaultValue = -1L
            },
            navArgument(ARG_HIDE_BOTTOM_BAR) {
                defaultValue = true
            }
        )
    ) {
        fun buildRoute(expenseId: Long): String = "$route?${NavArgs.EXPENSE_ID}=$expenseId"
    }

    object CashFlowDetails : Destination(
        route = "cashFlowDetails",
        label = R.string.cash_flow_details,
        arguments = listOf(
            navArgument(NavArgs.AGENT_ID) {
                type = NavType.LongType
                nullable = false
                defaultValue = -1L
            },
            navArgument(ARG_HIDE_BOTTOM_BAR) {
                defaultValue = true
            }
        )
    ) {
        fun buildRoute(agentId: Long): String = "$route?${NavArgs.AGENT_ID}=$agentId"
    }

    object Settings : Destination(
        route = "settings",
        label = R.string.settings,
        arguments = listOf(
            navArgument(ARG_HIDE_BOTTOM_BAR) {
                defaultValue = true
            }
        )
    )
}

val BottomNavDestinations = listOf<Destination>(
    Destination.CashFlow,
    Destination.Expenses,
    Destination.ExpensePlans
)

const val ARG_HIDE_BOTTOM_BAR = "hideBottomBar"