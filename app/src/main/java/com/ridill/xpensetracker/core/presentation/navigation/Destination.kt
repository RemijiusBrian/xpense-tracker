package com.ridill.xpensetracker.core.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ridill.xpensetracker.R

sealed class Destination(
    val route: String,
    @StringRes val label: Int,
    val icon: ImageVector? = null,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Expenses : Destination(
        route = "expenses",
        label = R.string.app_name,
        icon = Icons.Outlined.List
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