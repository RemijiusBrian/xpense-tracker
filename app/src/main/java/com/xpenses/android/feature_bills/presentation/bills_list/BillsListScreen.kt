package com.xpenses.android.feature_bills.presentation.bills_list

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.text.style.TextOverflow
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
import com.xpenses.android.feature_bills.presentation.add_bill.ADD_BILL_RESULT
import com.xpenses.android.feature_bills.presentation.components.CategoryIcon

@Composable
fun BillsListScreen(navController: NavController) {
    val viewModel: BillsListViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(BillsListState.INITIAL)

    val snackbarController = rememberSnackbarController()
    val context = LocalContext.current
    val navBackStackEntry = navController.currentBackStackEntry

    val addBillResult = navBackStackEntry?.savedStateHandle
        ?.getLiveData<String>(ADD_BILL_RESULT)?.observeAsState()
    LaunchedEffect(addBillResult) {
        navBackStackEntry?.savedStateHandle
            ?.remove<String>(ADD_BILL_RESULT)
        addBillResult?.value?.let(viewModel::onAddBillResult)
    }

    LaunchedEffect(context) {
        viewModel.events.collect { event ->
            when (event) {
                is BillsListViewModel.BillsListEvent.ShowSnackbar -> {
                    snackbarController.showSnackbar(event.message.asString(context))
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
        actions = viewModel,
        context = context
    )
}

@Composable
private fun ScreenContent(
    state: BillsListState,
    snackbarController: SnackbarController,
    onAddBillClick: () -> Unit,
    navigateUp: () -> Unit,
    actions: BillsListActions,
    context: Context
) {
    val lazyListState = rememberLazyListState()
    val isFabExpanded by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex < 1 }
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
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                top = PaddingMedium,
                bottom = ListPaddingLarge
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingMedium)
        ) {
            item {
                BillsGrid(bills = state.billsList)
            }
            BillState.values().forEach { billState ->
                val listForState = state.billPayments.filter { it.state == billState }
                if (listForState.isNotEmpty()) {
                    item(key = billState) {
                        ListLabel(
                            label = billState.label,
                            modifier = Modifier
                                .padding(horizontal = PaddingSmall)
                                .animateItemPlacement()
                        )
                    }
                    items(listForState, key = { it.id }) { payment ->
                        BillPayment(
                            category = payment.category,
                            name = payment.name,
                            date = payment.paymentOrPayByDate,
                            amount = payment.amountFormatted,
                            state = billState,
                            onMarkAsPaidClick = {
                                val category = context.getString(payment.category.label)
                                val paymentName = context.getString(
                                    R.string.bill_payment_name, category, payment.name
                                )
                                actions.onMarkAsPaidClick(payment.copy(name = paymentName))
                            },
                            modifier = Modifier
                                .animateItemPlacement()
                        )
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
            .heightIn(min = BillsGridHeight),
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
                    end = ListPaddingLarge,
                    start = PaddingMedium
                ),
                horizontalArrangement = Arrangement.spacedBy(SpacingMedium),
                verticalArrangement = Arrangement.spacedBy(SpacingSmall)
            ) {
                items(bills, key = { it.id }) { bill ->
                    BillCard(
                        icon = bill.category.icon,
                        name = bill.name,
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
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .aspectRatio(BillCardAspectRatio, true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = name,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(SpacingSmall))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
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
                CategoryIcon(category = category)
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
            if (state != BillState.PAID) {
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

private const val BillCardAspectRatio = 1.25.toFloat() / 1f
private val BillsGridHeight = 160.dp

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
                        1000.0,
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
            },
            context = LocalContext.current
        )
    }
}