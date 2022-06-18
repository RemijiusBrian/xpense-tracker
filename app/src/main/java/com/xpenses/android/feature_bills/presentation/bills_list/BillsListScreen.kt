package com.xpenses.android.feature_bills.presentation.bills_list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xpenses.android.R
import com.xpenses.android.core.ui.components.*
import com.xpenses.android.core.ui.navigation.screen_specs.AddBillScreenSpec
import com.xpenses.android.core.ui.navigation.screen_specs.BillsListScreenSpec
import com.xpenses.android.core.ui.theme.*
import com.xpenses.android.core.util.exhaustive
import com.xpenses.android.feature_bills.domain.model.BillCategory
import com.xpenses.android.feature_bills.domain.model.BillItem
import com.xpenses.android.feature_bills.domain.model.BillPayment
import com.xpenses.android.feature_bills.domain.model.BillState

@Composable
fun BillsListScreen(navController: NavController) {
    val viewModel: BillsListViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(BillsListState.INITIAL)

    val snackbarController = rememberSnackbarController()
    val context = LocalContext.current

    LaunchedEffect(context) {
        viewModel.events.collect { event ->
            when (event) {
                BillsListViewModel.BillsListEvent.PaymentMarkedAsPaid -> {
                    snackbarController.showSnackbar(context.getString(R.string.bill_marked_as_paid_message))
                }
            }.exhaustive
        }
    }

    ScreenContent(
        state = state,
        snackbarController = snackbarController,
        onAddBillClick = {
            navController.navigate(AddBillScreenSpec.buildRoute())
        },
        navigateUp = navController::popBackStack,
        actions = viewModel
    )
}

@Composable
private fun ScreenContent(
    state: BillsListState,
    snackbarController: SnackbarController,
    onAddBillClick: () -> Unit,
    navigateUp: () -> Unit,
    actions: BillsListActions
) {
    val lazyListState = rememberLazyListState()
    val isFabExpanded by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex < 3 }
    }

    Scaffold(
        snackbarHost = { XTSnackbarHost(snackbarController) },
        topBar = {
            SmallTopAppBar(
                title = { Text(stringResource(BillsListScreenSpec.label)) },
                navigationIcon = {
                    BackArrowButton(onClick = navigateUp)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
                BillState.values().forEach { billState ->
                    val listForState = state.billPayments.filter { it.state == billState }
                    if (listForState.isNotEmpty()) {
                        item(key = billState) {
                            ListLabel(
                                label = billState.label,
                                modifier = Modifier
                                    .animateItemPlacement()
                            )
                        }
                        items(listForState, key = { it.id }) { payment ->
                            BillPayment(
                                category = payment.category,
                                name = payment.name,
                                date = payment.paymentOrPayByDate,
                                amount = payment.amount,
                                state = billState,
                                onMarkAsPaidClick = { actions.onMarkAsPaidClick(payment) },
                                modifier = Modifier
                                    .animateItemPlacement()
                            )
                        }
                    }
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
private fun BillPayment(
    category: BillCategory,
    name: String,
    amount: String,
    date: String,
    state: BillState,
    onMarkAsPaidClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingSmall)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                        painter = painterResource(category.icon),
                        contentDescription = stringResource(category.label),
                        modifier = Modifier
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(SpacingMedium))
                Column(
                    modifier = Modifier
                        .weight(WEIGHT_1)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(state.displayMessage, date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = amount,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (state != BillState.PAID && expanded) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    TextButton(onClick = onMarkAsPaidClick) {
                        Text(stringResource(R.string.mark_as_paid))
                    }
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
                billPayments = (1..10).map {
                    BillPayment(
                        it.toLong(),
                        "10-10",
                        "1000",
                        BillCategory.BROADBAND,
                        name = "Name",
                        state = BillState.UPCOMING
                    )
                }
            ),
            snackbarController = rememberSnackbarController(),
            onAddBillClick = {},
            navigateUp = {},
            actions = object : BillsListActions {
                override fun onMarkAsPaidClick(payment: BillPayment) {}
            }
        )
    }
}