package dev.ridill.xpensetracker.core.ui.navigation.screen_specs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.rememberSnackbarController
import dev.ridill.xpensetracker.core.util.exhaustive
import dev.ridill.xpensetracker.feature_bills.presentation.add_edit_bill.*

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

    fun getBillIdFromSavedStateHandle(savedStateHandle: SavedStateHandle): Long? =
        savedStateHandle.get<Long>(ARG_BILL_ID)

    fun isEditMode(billId: Long?): Boolean = billId != null && billId != NO_BILL_ID

    fun buildRoute(billId: Long = NO_BILL_ID): String = "add_bill?$ARG_BILL_ID=$billId"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: AddEditBillViewModel = hiltViewModel()
        val description by viewModel.name.observeAsState("")
        val amount by viewModel.amount.observeAsState("")
        val state by viewModel.state.observeAsState(AddEditBillState.INITIAL)

        val context = LocalContext.current
        val snackbarController = rememberSnackbarController()

        LaunchedEffect(context) {
            @Suppress("IMPLICIT_CAST_TO_ANY")
            viewModel.events.collect { event ->
                when (event) {
                    AddEditBillViewModel.AddEditBillEvent.BillAdded -> {
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(ADD_EDIT_BILL_RESULT, RESULT_BILL_ADDED)
                        navController.popBackStack()
                    }
                    AddEditBillViewModel.AddEditBillEvent.BillDeleted -> {
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(ADD_EDIT_BILL_RESULT, RESULT_BILL_DELETED)
                        navController.popBackStack()
                    }
                    AddEditBillViewModel.AddEditBillEvent.BillUpdated -> {
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(ADD_EDIT_BILL_RESULT, RESULT_BILL_UPDATED)
                        navController.popBackStack()
                    }
                    is AddEditBillViewModel.AddEditBillEvent.ShowSnackbar -> {
                        snackbarController.showSnackbar(event.message.asString(context))
                    }
                }.exhaustive
            }
        }

        AddEditBillScreenContent(
            name = description,
            amount = amount,
            state = state,
            context = context,
            snackbarController = snackbarController,
            actions = viewModel,
            navigateUp = navController::popBackStack,
            isEditMode = viewModel.isEditMode
        )
    }
}

private const val ARG_BILL_ID = "billId"
private const val NO_BILL_ID = -1L