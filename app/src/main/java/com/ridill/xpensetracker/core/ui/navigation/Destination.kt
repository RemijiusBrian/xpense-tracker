package com.ridill.xpensetracker.core.ui.navigation

import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ridill.xpensetracker.R

sealed class Destination(
    val route: String,
    @StringRes val label: Int,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Dashboard : Destination(
        route = "dashboard",
        label = R.string.app_name,
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
        )
    ) {
        fun buildRoute(expenseId: Long): String = "$route?${NavArgs.EXPENSE_ID}=$expenseId"
    }

    object CashFlowDetails : Destination(
        route = "cashFlowDetails",
        label = R.string.cash_flow_details,
        arguments = listOf(
            navArgument(NavArgs.CASH_FLOW_EXPENSE) {
                type = NavType.LongType
                nullable = false
                defaultValue = -1L
            }
        )
    ) {
        fun buildRoute(expenseId: Long): String = "$route?${NavArgs.CASH_FLOW_EXPENSE}=$expenseId"
    }
}