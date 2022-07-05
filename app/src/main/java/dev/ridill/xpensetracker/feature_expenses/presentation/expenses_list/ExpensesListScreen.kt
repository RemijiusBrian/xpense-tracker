package dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.*
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.BottomBarScreenSpec
import dev.ridill.xpensetracker.core.ui.theme.*
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.ui.util.numberSliderTransition
import dev.ridill.xpensetracker.core.ui.util.slideInHorizontallyWithFadeIn
import dev.ridill.xpensetracker.core.ui.util.slideOutHorizontallyWithFadeOut
import dev.ridill.xpensetracker.core.util.Constants
import dev.ridill.xpensetracker.feature_expenses.domain.model.ExpenseListItem
import dev.ridill.xpensetracker.feature_expenses.domain.model.MonthStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ExpenseListScreenContent(
    state: ExpensesState,
    snackbarController: SnackbarController,
    actions: ExpensesActions,
    navigateToBottomBarDestination: (BottomBarScreenSpec) -> Unit
) {
    val topBarScrollState = rememberTopAppBarScrollState()
    val decayAnimation = rememberSplineBasedDecay<Float>()
    val topAppBarScrollBehavior = remember {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            decayAnimationSpec = decayAnimation,
            state = topBarScrollState
        )
    }

    Scaffold(
        snackbarHost = { XTSnackbarHost(snackbarController) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = state.expenditureLimit > 0L,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                BottomAppBar(
                    icons = {
                        BottomBarScreenSpec.screens.forEach { screen ->
                            IconButton(
                                onClick = {
                                    navigateToBottomBarDestination(screen)
                                }
                            ) {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = stringResource(screen.label)
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = actions::onAddFabClick
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = stringResource(R.string.content_description_add_expense)
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background,
                    tonalElevation = ZeroDp
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Greetings(
                limit = state.expenditureLimit,
                onLimitUpdate = actions::onExpenditureLimitUpdateClick
            )
            Stats(
                monthlyBalance = state.balance,
                monthStats = state.monthsToExpenditurePercents,
                selectedMonth = state.selectedMonth,
                onMonthSelect = actions::onMonthSelect,
                modifier = Modifier
                    .fillMaxHeight(MONTHS_BARS_HEIGHT_PERCENT)
            )
            AnimatedVisibility(visible = state.tags.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        top = PaddingSmall,
                        bottom = PaddingSmall,
                        end = ListPaddingLarge,
                        start = PaddingMedium
                    ),
                    horizontalArrangement = Arrangement.spacedBy(SpacingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        AnimatedVisibility(
                            visible = !state.tagDeletableModeActive,
                            enter = slideInHorizontallyWithFadeIn(false),
                            exit = slideOutHorizontallyWithFadeOut(false)
                        ) {
                            FilterChip(
                                selected = state.selectedTag.isEmpty(),
                                onClick = { actions.onTagFilterSelect(Constants.STRING_ALL) },
                                label = { Text(Constants.STRING_ALL) }
                            )
                        }
                    }
                    items(state.tags, key = { it }) { tag ->
                        TagItem(
                            selected = tag == state.selectedTag,
                            label = tag,
                            onClick = { actions.onTagFilterSelect(tag) },
                            onLongClick = actions::onTagLongClick,
                            deleteModeActive = state.tagDeletableModeActive,
                            onDelete = { actions.onTagDeleteClick(tag) },
                            modifier = Modifier
                                .animateItemPlacement()
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(WEIGHT_1),
                contentAlignment = Alignment.Center
            ) {
                if (state.expenses.isEmpty()) {
                    ListEmptyIndicator(message = R.string.empty_expenses_data_message)
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(SpacingSmall),
                        contentPadding = PaddingValues(
                            bottom = ListPaddingLarge,
                            start = PaddingMedium,
                            end = PaddingMedium
                        ),
                        modifier = Modifier
                            .matchParentSize()
                            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                    ) {
                        item {
                            ListLabel(label = R.string.label_expense_list)
                        }
                        items(state.expenses, key = { it.id }) { expense ->
                            ExpenseItem(
                                name = expense.name,
                                date = expense.date,
                                amount = expense.amount,
                                onClick = { actions.onExpenseClick(expense.id) },
                                modifier = Modifier
                                    .animateItemPlacement()
                            )
                        }
                    }
                }
            }
        }

        if (state.showExpenditureLimitUpdateDialog) {
            ExpenditureLimitUpdateDialog(
                previousLimit = TextUtil.formatAmountWithCurrency(state.expenditureLimit),
                onDismiss = actions::onExpenditureLimitUpdateDialogDismiss,
                onConfirm = actions::onExpenditureLimitUpdateDialogConfirm
            )
        }

        if (state.showTagDeleteConfirmation) {
            SimpleConfirmationDialog(
                title = R.string.delete_tagged_expenses_question,
                text = R.string.delete_tagged_expenses_message,
                onDismiss = actions::onDeleteExpensesWithTagDismiss,
                onConfirm = actions::onDeleteExpensesWithTagConfirm,
                icon = Icons.Rounded.DeleteForever
            )
        }
    }
}

@Composable
private fun Greetings(
    limit: Long,
    onLimitUpdate: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = PaddingMedium, vertical = PaddingSmall)
    ) {
        Text(
            text = stringResource(R.string.greeting_comma),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.SemiBold
        )
        if (limit <= 0) {
            TextButton(onClick = onLimitUpdate) {
                Text(stringResource(R.string.click_here_to_set_limit))
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.expenditure_limit_label),
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = onLimitUpdate) {
                    AnimatedContent(
                        targetState = limit,
                        transitionSpec = {
                            numberSliderTransition { targetState > initialState }
                        }
                    ) { value ->
                        Text(
                            text = TextUtil.formatAmountWithCurrency(value),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Stats(
    monthlyBalance: Double,
    monthStats: List<MonthStats>,
    selectedMonth: String,
    onMonthSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(PaddingMedium),
        color = MaterialTheme.colorScheme.surfaceVariant
            .copy(alpha = ContentAlpha.PERCENT_16),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingSmall)
        ) {
            Balance(balance = monthlyBalance)
            Spacer(Modifier.height(SpacingSmall))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(WEIGHT_1),
                contentAlignment = Alignment.Center
            ) {
                if (monthStats.isEmpty()) {
                    DataEmptyIndicator(
                        iconRes = R.drawable.ic_bar_data,
                        message = R.string.error_no_data_yet
                    )
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(SpacingSmall),
                        verticalAlignment = Alignment.Bottom,
                        contentPadding = PaddingValues(
                            start = PaddingMedium,
                            end = ListPaddingLarge
                        )
                    ) {
                        items(items = monthStats, key = { it.month }) { monthStat ->
                            MonthBar(
                                month = monthStat.monthParsed,
                                selected = monthStat.month == selectedMonth,
                                spentAmount = monthStat.expenditureAmount,
                                expenditurePercentage = monthStat.expenditurePercent,
                                onClick = { onMonthSelect(monthStat.month) },
                                modifier = Modifier
                                    .fillParentMaxHeight()
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
private fun Balance(
    balance: Double,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = balance > 0) {
        Column(
            modifier = modifier
                .padding(horizontal = PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.balance),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
                    .copy(alpha = ContentAlpha.PERCENT_60)
            )
            AnimatedContent(
                targetState = balance,
                transitionSpec = {
                    numberSliderTransition { targetState > initialState }
                }
            ) { amount ->
                Text(
                    text = TextUtil.formatAmountWithCurrency(amount),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
private fun MonthBar(
    month: String,
    selected: Boolean,
    spentAmount: String,
    expenditurePercentage: Float,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val barTransition = updateTransition(
        targetState = selected,
        label = stringResource(R.string.anim_label_month_bar)
    )
    val scale by barTransition.animateFloat(label = stringResource(R.string.anim_label_month_bar_scale)) { isSelected ->
        if (isSelected) 1f else 0.80f
    }
    val color by barTransition.animateColor(label = stringResource(R.string.anim_label_month_bar_color)) { isSelected ->
        when {
            expenditurePercentage >= Constants.ONE_F -> MaterialTheme.colorScheme.errorContainer
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
        }
    }

    val coroutineScope = rememberCoroutineScope()
    var showExpenditureAmount by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .weight(WEIGHT_1)
                .widthIn(min = MonthBarMinWidth)
                .clip(MaterialTheme.shapes.small)
                .drawBehind {
                    val heightPercent = size.height * expenditurePercentage
                        .coerceAtMost(Constants.ONE_F)
                    scale(scaleX = Constants.ONE_F, scaleY = scale) {
                        drawRoundRect(
                            color = color,
                            cornerRadius = CornerRadius(8f, 8f),
                            topLeft = Offset(
                                x = 0f,
                                y = size.height - heightPercent
                            )
                        )
                    }
                }
                .combinedClickable(
                    onClick = {
                        if (!selected) onClick()
                    },
                    onLongClick = {
                        coroutineScope.launch {
                            showExpenditureAmount = true
                            delay(EXPENDITURE_AMOUNT_DISPLAY_DURATION)
                            showExpenditureAmount = false
                        }
                    }
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            this@Column.AnimatedVisibility(
                visible = showExpenditureAmount,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .width(MonthBarMinWidth),
                    shadowElevation = ElevationSmall
                ) {
                    Text(
                        text = stringResource(R.string.spent_amount, spentAmount),
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                text = "${(expenditurePercentage * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = contentColorFor(color)
            )
        }
        Spacer(Modifier.height(SpacingXSmall))
        Text(
            text = month,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TagItem(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    deleteModeActive: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = FilterChipDefaults.filterChipColors()
            .containerColor(enabled = true, selected = selected).value,
        modifier = modifier
            .padding(vertical = PaddingSmall)
            .semantics { role = Role.Checkbox }
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = MaterialTheme.shapes.small,
        border = FilterChipDefaults.filterChipBorder()
            .borderStroke(enabled = true, selected = selected).value,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .defaultMinSize(minHeight = FilterChipDefaults.Height)
                .padding(horizontal = PaddingSmall)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(horizontal = PaddingSmall)
            )
            AnimatedVisibility(visible = deleteModeActive) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(FilterChipDefaults.IconSize)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.content_description_delete_tag)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    name: String,
    date: String,
    amount: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(PaddingMedium)
        ) {
            Column(
                modifier = Modifier
                    .weight(WEIGHT_1)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = amount,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ExpenditureLimitUpdateDialog(
    previousLimit: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onConfirm(input) }) {
                Text(stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_piggy_bank),
                contentDescription = null
            )
        },
        title = {
            Text(stringResource(R.string.enter_expenditure_limit))
        },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                singleLine = true,
                placeholder = {
                    Text(previousLimit)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                shape = MaterialTheme.shapes.medium
            )
        }
    )
}

private const val MONTHS_BARS_HEIGHT_PERCENT = 0.36f
private val MonthBarMinWidth = 40.dp
private const val EXPENDITURE_AMOUNT_DISPLAY_DURATION = 5000L

@Preview(showBackground = true)
@Composable
private fun PreviewScreenContent() {
    XpenseTrackerTheme {
        ExpenseListScreenContent(
            state = ExpensesState(
                tags = listOf("Bills", "Leisure"),
                selectedTag = Constants.STRING_ALL,
                monthsToExpenditurePercents = emptyList() /*(1..5).map {
                    MonthAndExpenditurePercent("Month $it", it * 0.1f)
                }*/,
                selectedMonth = "Month 1",
                expenses = (1..20).map {
                    ExpenseListItem(
                        id = it.toLong(),
                        name = "Expense $it",
                        amount = "${it * 100}",
                        date = "Monday, 10th"
                    )
                },
                expenditureLimit = 1000L
            ),
            snackbarController = rememberSnackbarController(),
            actions = object : ExpensesActions {
                override fun onExpenditureLimitUpdateClick() {}
                override fun onExpenditureLimitUpdateDialogDismiss() {}
                override fun onExpenditureLimitUpdateDialogConfirm(limit: String) {}
                override fun onTagFilterSelect(tag: String) {}
                override fun onTagLongClick() {}
                override fun onTagDeleteClick(tag: String) {}
                override fun onDeleteExpensesWithTagDismiss() {}
                override fun onDeleteExpensesWithTagConfirm() {}
                override fun onAddFabClick() {}
                override fun onMonthSelect(month: String) {}
                override fun onExpenseClick(id: Long) {}
            },
            navigateToBottomBarDestination = {}
        )
    }
}