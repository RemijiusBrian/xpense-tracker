package dev.ridill.xpensetracker.feature_bills.presentation.bills_list

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.*
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.BillsListScreenSpec
import dev.ridill.xpensetracker.core.ui.theme.*
import dev.ridill.xpensetracker.feature_bills.domain.model.BillCategory
import dev.ridill.xpensetracker.feature_bills.domain.model.BillItem
import dev.ridill.xpensetracker.feature_bills.domain.model.BillPayment
import dev.ridill.xpensetracker.feature_bills.domain.model.BillState
import dev.ridill.xpensetracker.feature_bills.presentation.components.BillCategoryIcon

@Composable
fun BillsListScreenContent(
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
                navigationIcon = { BackArrowButton(onClick = navigateUp) },
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
    ) { paddingValues ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                top = SpacingMedium,
                bottom = ListPaddingLarge
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingMedium)
        ) {
            item(key = "Bills Grid") {
                BillsGrid(
                    bills = state.billsList,
                    onBillClick = actions::onBillClick
                )
            }
            state.billPayments.forEach { (state, payments) ->
                item(key = state) {
                    ListLabel(
                        label = state.label,
                        modifier = Modifier
                            .padding(horizontal = SpacingSmall)
                            .animateItemPlacement()
                    )
                }
                items(items = payments, key = { it.id }) { payment ->
                    BillPayment(
                        category = payment.category,
                        name = payment.name,
                        date = payment.dateFormatted,
                        amount = payment.amountFormatted,
                        state = state,
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

@Composable
private fun BillsGrid(
    bills: Map<BillCategory, List<BillItem>>,
    onBillClick: (Long) -> Unit,
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
                ),
                horizontalArrangement = Arrangement.spacedBy(SpacingSmall),
                verticalArrangement = Arrangement.spacedBy(SpacingSmall)
            ) {
                bills.forEach { (category, list) ->
                    item(
                        span = { GridItemSpan(maxLineSpan) },
                        key = category.name,
                        contentType = BillCategory::class.java
                    ) {
                        BillSeparator(
                            category = category,
                            modifier = Modifier
                                .animateItemPlacement()
                        )
                    }
                    items(
                        items = list,
                        key = { it.id },
                        contentType = { BillItem::class.java }
                    ) { bill ->
                        BillCard(
                            name = bill.name,
                            modifier = Modifier
                                .animateItemPlacement(),
                            onClick = { onBillClick(bill.id) },
                            payBy = bill.dueDate
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BillSeparator(
    category: BillCategory,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .rotate(-90f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(category.label),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.width(SpacingMedium))
        Icon(
            painter = painterResource(category.icon),
            contentDescription = stringResource(category.label),
            modifier = Modifier
                .rotate(90f)
        )
    }
}

@Composable
private fun BillCard(
    name: String,
    onClick: () -> Unit,
    payBy: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .widthIn(max = BillCardMaxWidth),
        onClick = onClick,
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.due_date_value, payBy),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
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
            .padding(horizontal = SpacingMedium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingSmall)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BillCategoryIcon(category = category)
                Spacer(Modifier.width(SpacingMedium))
                Column(
                    modifier = Modifier
                        .weight(WEIGHT_1)
                ) {
                    Text(
                        text = "${stringResource(category.label)} ($name)",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(state.displayMessage, date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.width(SpacingMedium))
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

private val BillsGridHeight = 172.dp
private val BillCardMaxWidth = 160.dp

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewScreenContent() {
    val list = (1..5).map {
        BillItem(it.toLong(), "Bill $it", BillCategory.WATER, "100", "Tue 10")
    }
    XpenseTrackerTheme {
        BillsListScreenContent(
            state = BillsListState(
                billsList = list.groupBy { it.category },
                billPayments = (1..10).map {
                    BillPayment(
                        it.toLong(),
                        payByDateMillis = System.currentTimeMillis(),
                        amount = 1000.0,
                        category = BillCategory.BROADBAND,
                        name = "Name",
                        state = BillState.UPCOMING
                    )
                }.groupBy { it.state }
            ),
            snackbarController = rememberSnackbarController(),
            onAddBillClick = {},
            navigateUp = {},
            actions = object : BillsListActions {
                override fun onMarkAsPaidClick(payment: BillPayment) {}
                override fun onBillClick(id: Long) {}
            },
            context = LocalContext.current
        )
    }
}