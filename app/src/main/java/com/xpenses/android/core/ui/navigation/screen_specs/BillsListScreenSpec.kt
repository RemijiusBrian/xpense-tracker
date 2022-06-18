package com.xpenses.android.core.ui.navigation.screen_specs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.xpenses.android.R
import com.xpenses.android.feature_bills.presentation.bills_list.BillsListScreen

object BillsListScreenSpec : BottomBarScreenSpec {

    override val icon: ImageVector = Icons.Default.ReceiptLong

    override val label: Int = R.string.destination_bills

    override val navHostRoute: String = "bills_list"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        BillsListScreen(navController = navController)
    }
}