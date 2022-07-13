package dev.ridill.xpensetracker.core.ui.navigation.screen_specs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.rememberSnackbarController
import dev.ridill.xpensetracker.core.util.exhaustive
import dev.ridill.xpensetracker.feature_bills.presentation.add_edit_bill.ADD_EDIT_BILL_RESULT
import dev.ridill.xpensetracker.feature_bills.presentation.bills_list.BillsListScreenContent
import dev.ridill.xpensetracker.feature_bills.presentation.bills_list.BillsListState
import dev.ridill.xpensetracker.feature_bills.presentation.bills_list.BillsListViewModel

object BillsListScreenSpec : BottomBarScreenSpec {

    override val icon: ImageVector = Icons.Outlined.ReceiptLong

    override val label: Int = R.string.destination_bills

    override val navHostRoute: String = "bills_list"

    override val deepLinks: List<NavDeepLink>
        get() = listOf(
            navDeepLink {
                uriPattern = "https:/www.xpensetracker.ridll.dev/bills_list"
            }
        )

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: BillsListViewModel = hiltViewModel()
        val state by viewModel.state.observeAsState(BillsListState.INITIAL)

        val snackbarController = rememberSnackbarController()
        val context = LocalContext.current

        val addBillResult = navBackStackEntry.savedStateHandle
            .getLiveData<String>(ADD_EDIT_BILL_RESULT).observeAsState()
        LaunchedEffect(addBillResult) {
            navBackStackEntry.savedStateHandle
                .remove<String>(ADD_EDIT_BILL_RESULT)
            addBillResult.value?.let(viewModel::onAddBillResult)
        }

        LaunchedEffect(context) {
            viewModel.events.collect { event ->
                when (event) {
                    is BillsListViewModel.BillsListEvent.ShowUiMessage -> {
                        snackbarController.showMessageSnackbar(event.message.asString(context))
                    }
                    is BillsListViewModel.BillsListEvent.NavigateToAddEditBillScreen -> {
                        navController.navigate(AddEditBillScreenSpec.buildRoute(event.id))
                    }
                }.exhaustive
            }
        }

        BillsListScreenContent(
            state = state,
            snackbarController = snackbarController,
            onAddBillClick = {
                navController.navigate(AddEditBillScreenSpec.buildRoute())
            },
            navigateUp = navController::popBackStack,
            actions = viewModel,
            context = context
        )
    }
}