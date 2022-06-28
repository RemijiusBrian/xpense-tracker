package com.xpenses.android.core.ui.navigation.screen_specs

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import com.xpenses.android.R
import com.xpenses.android.feature_bills.presentation.add_edit_bill.AddEditBillScreen

object AddEditBillScreenSpec : ScreenSpec {

    override val label: Int = R.string.destination_add_edit_bill

    override val navHostRoute: String = "add_bill?$ARG_BILL_ID={$ARG_BILL_ID}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(ARG_BILL_ID) {
                type = NavType.LongType
                nullable = false
                defaultValue = NO_BILL_ID
            }
        )

    fun getBillIdFromSavedStateHandle(savedStateHandle: SavedStateHandle): Long? {
        val id = savedStateHandle.get<Long>(ARG_BILL_ID)
        println("AppDebug: Bill Id = $id")
        return id
    }

    fun isEditMode(billId: Long?): Boolean = billId != null && billId != NO_BILL_ID

    fun buildRoute(billId: Long = NO_BILL_ID): String = "add_bill?$ARG_BILL_ID=$billId"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        AddEditBillScreen(navController = navController)
    }
}

private const val ARG_BILL_ID = "billId"
private const val NO_BILL_ID = -1L