package com.xpenses.android.feature_bills.presentation.bills_list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xpenses.android.R
import com.xpenses.android.core.ui.components.*
import com.xpenses.android.core.ui.navigation.screen_specs.AddBillScreenSpec
import com.xpenses.android.core.ui.theme.*
import com.xpenses.android.feature_bills.domain.model.BillCategory
import com.xpenses.android.feature_bills.domain.model.BillItem

@Composable
fun BillsListScreen(navController: NavController) {

    val viewModel: BillsListViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(BillsListState.INITIAL)

    val snackbarController = rememberSnackbarController()

    ScreenContent(
        state = state,
        snackbarController = snackbarController,
        onAddBillClick = {
            navController.navigate(AddBillScreenSpec.buildRoute())
        },
        navigateUp = navController::popBackStack
    )
}

@Composable
private fun ScreenContent(
    state: BillsListState,
    snackbarController: SnackbarController,
    onAddBillClick: () -> Unit,
    navigateUp: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val isFabExpanded by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex < 3 }
    }

    Scaffold(
        snackbarHost = { XTSnackbarHost(snackbarController) },
        topBar = {
            SmallTopAppBar(
                title = { Text(stringResource(AddBillScreenSpec.label)) },
                navigationIcon = {
                    BackArrowButton(onClick = navigateUp)
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddBillClick,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Receipt,
                        contentDescription = null
                    )
                },
                text = { Text(stringResource(R.string.new_bill)) },
                expanded = isFabExpanded
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BillsGrid(bills = state.billsList)
            Spacer(Modifier.height(SpacingMedium))
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .weight(WEIGHT_1),
                contentPadding = PaddingValues(
                    start = PaddingMedium,
                    end = PaddingMedium,
                    bottom = ListPaddingLarge
                ),
                verticalArrangement = Arrangement.spacedBy(SpacingMedium)
            ) {
                item {
                    ListLabel(label = R.string.label_upcoming_payments)
                }
                items(state.upcomingPayments) { payment ->
                    UpcomingPayment(
                        icon = payment.category.icon,
                        name = payment.name,
                        amount = payment.amount,
                        onMarkAsPaidClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun BillsGrid(
    bills: List<BillItem>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(BillsGridHeightFraction),
        contentAlignment = Alignment.Center
    ) {
        if (bills.isEmpty()) {
            GridEmptyIndicator(message = R.string.empty_bills_data_message)
        } else {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .matchParentSize(),
                contentPadding = PaddingValues(
                    top = PaddingMedium,
                    bottom = PaddingMedium,
                    start = PaddingMedium,
                    end = ListPaddingLarge
                ),
                horizontalArrangement = Arrangement.spacedBy(SpacingMedium),
                verticalArrangement = Arrangement.spacedBy(SpacingSmall)
            ) {
                items(bills, key = { it.id }) { bill ->
                    BillCard(
                        icon = bill.category.icon,
                        name = bill.name,
                        amount = bill.amount,
                        modifier = Modifier
                            .animateItemPlacement()
                    )
                }
            }
        }
    }
}

@Composable
private fun BillCard(
    @DrawableRes icon: Int,
    name: String,
    amount: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .aspectRatio(BillCardAspectRatio, true)
    ) {
        Column(
            modifier = Modifier
                .padding(PaddingMedium)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = name,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(SpacingMedium))
            Text(
                text = name
            )
            Text(text = amount)
        }
    }
}

@Composable
private fun UpcomingPayment(
    @DrawableRes icon: Int,
    name: String,
    amount: String,
    onMarkAsPaidClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingMedium)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant
                                .copy(alpha = ContentAlpha.PERCENT_16),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(PaddingMedium)
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(SpacingSmall))
                Text(text = name)
                Text(text = amount)
                TextButton(onClick = onMarkAsPaidClick) {
                    Text(stringResource(R.string.mark_as_paid))
                }
            }
        }
    }
}

private const val BillCardAspectRatio = 1 / 1.25f
private const val BillsGridHeightFraction = 0.32f

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewScreenContent() {
    val list = (1..5).map {
        BillItem(it.toLong(), "Bill $it", BillCategory.WATER, "100")
    }
    XpenseTrackerTheme {
        ScreenContent(
            state = BillsListState(
                billsList = list,
                upcomingPayments = list
            ),
            snackbarController = rememberSnackbarController(),
            onAddBillClick = {},
            navigateUp = {}
        )
    }
}