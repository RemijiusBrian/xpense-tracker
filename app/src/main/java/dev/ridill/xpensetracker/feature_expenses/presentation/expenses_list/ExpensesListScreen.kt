package dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.VectorConverter
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.DeleteForever
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.*
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.BottomBarScreenSpec
import dev.ridill.xpensetracker.core.ui.theme.*
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.ui.util.verticalSpinnerAnimation
import dev.ridill.xpensetracker.core.util.Constants
import dev.ridill.xpensetracker.feature_expenses.domain.model.ExpenseListItem
import dev.ridill.xpensetracker.feature_expenses.domain.model.MonthStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExpenseListScreenContent(
    state: ExpensesState,
    snackbarController: SnackbarController,
    actions: ExpensesActions,
    navigateToBottomBarDestination: (BottomBarScreenSpec) -> Unit,
    navigateToLimitUpdate: () -> Unit
) {
    val topBarScrollState = rememberTopAppBarScrollState()
    val topAppBarScrollBehavior = remember {
        TopAppBarDefaults.enterAlwaysScrollBehavior(topBarScrollState)
    }

    Scaffold(
        snackbarHost = { XTSnackbarHost(snackbarController) },
        topBar = {
            AnimatedContent(targetState = state.multiSelectionModeActive) { multiSelectionModeActive ->
                if (multiSelectionModeActive) {
                    SmallTopAppBar(
                        title = {
                            Text(
                                stringResource(
                                    R.string.count_selected,
                                    state.selectedExpenseIds.size
                                )
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = actions::onCancelMultiSelectionMode) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.content_description_cancel_multi_selection_mode)
                                )
                            }
                        },
                        actions = {
                            var expanded by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = stringResource(R.string.content_description_toggle_menu)
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(
                                                    if (state.areAllExpensesSelected) R.string.deselect_all
                                                    else R.string.select_all
                                                )
                                            )
                                        },
                                        onClick = {
                                            expanded = false
                                            actions.onSelectOrDeselectAllOptionClick(state.areAllExpensesSelected)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.option_delete)) },
                                        onClick = {
                                            expanded = false
                                            actions.onDeleteOptionClick()
                                        }
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                } else {
                    CenterAlignedTopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        scrollBehavior = topAppBarScrollBehavior
                    )
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = state.isLimitSet) {
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
                        FloatingActionButton(onClick = actions::onAddFabClick) {
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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                start = SpacingMedium,
                end = SpacingMedium,
                bottom = ListPaddingLarge,
                top = SpacingMedium
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingMedium)
        ) {
            item(key = "Monthly Balance") {
                MonthlyBalance(
                    balance = state.balance,
                    balancePercent = state.balancePercent,
                    showBalanceWarning = state.showLowBalanceWarning,
                    isLimitSet = state.isLimitSet,
                    onSetLimitClick = navigateToLimitUpdate
                )
            }
            item(key = "stats") {
                MonthlyStats(
                    monthStats = state.monthsToExpenditurePercents,
                    selectedMonth = state.selectedMonth,
                    onMonthSelect = actions::onMonthSelect
                )
            }

            item(key = "Expense Filters") {
                TagFilters(
                    tags = state.tags,
                    selectedTag = state.selectedTag,
                    onTagClick = actions::onTagFilterSelect,
                    onTagLongClick = actions::onTagLongClick,
                    onTagDelete = actions::onTagDeleteClick,
                    tagDeletionModeActive = state.tagDeletionModeActive
                )
            }

            if (state.expenses.isNotEmpty()) {
                item(key = "Expense List Label") {
                    ListLabel(label = R.string.expense_list_label)
                }
                items(state.expenses, key = { it.id }) { expense ->
                    ExpenseItem(
                        name = expense.name,
                        date = expense.date,
                        amount = expense.amount,
                        onClick = { actions.onExpenseClick(expense.id) },
                        onLongClick = { actions.onExpenseLongClick(expense.id) },
                        modifier = Modifier
                            .animateItemPlacement(),
                        selected = expense.id in state.selectedExpenseIds
                    )
                }
            } else {
                item(key = "Expenses Empty") {
                    Spacer(Modifier.height(SpacingLarge))
                    ListEmptyIndicator(
                        message = R.string.empty_expenses_data_message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                    )
                }
            }
        }

        if (state.showTagDeleteConfirmation) {
            SimpleConfirmationDialog(
                title = R.string.delete_tagged_expenses_title,
                text = R.string.delete_tagged_expenses_message,
                onDismiss = actions::onDeleteExpensesWithTagDismiss,
                onConfirm = actions::onDeleteExpensesWithTagConfirm,
                icon = Icons.Rounded.DeleteForever
            )
        }

        if (state.showDeleteExpensesConfirmation) {
            SimpleConfirmationDialog(
                title = R.string.delete_selected_expenses_title,
                text = R.string.delete_selected_expenses_message,
                onDismiss = actions::onDeleteExpensesDismiss,
                onConfirm = actions::onDeleteExpensesConfirm,
                icon = Icons.Outlined.DeleteForever
            )
        }
    }
}

@Composable
private fun MonthlyBalance(
    balance: Double,
    balancePercent: Float,
    showBalanceWarning: Boolean,
    isLimitSet: Boolean,
    onSetLimitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val vectorConverter = remember { Color.VectorConverter(ColorSpaces.Srgb) }
    val colorAnimation = remember {
        TargetBasedAnimation(
            animationSpec = tween(),
            typeConverter = vectorConverter,
            initialValue = errorColor,
            targetValue = primaryColor,
            initialVelocity = primaryColor
        )
    }
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.greeting_comma),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.SemiBold
        )
        if (isLimitSet) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.monthly_balance_is),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(SpacingSmall))
                AnimatedContent(
                    targetState = balance,
                    transitionSpec = { verticalSpinnerAnimation { targetState > initialState } }
                ) { value ->
                    Text(
                        text = TextUtil.formatAmountWithCurrency(value),
                        style = MaterialTheme.typography.titleLarge,
                        color = colorAnimation.getValueFromNanos(
                            (balancePercent * colorAnimation.durationNanos).toLong()
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(SpacingSmall))
                BalanceLowWarning(
                    show = showBalanceWarning,
                    modifier = Modifier
                        .align(Alignment.Top)
                )
            }
        } else {
            TextButton(onClick = onSetLimitClick) {
                Text(stringResource(R.string.click_here_to_set_limit))
            }
        }
    }
}

@Composable
private fun BalanceLowWarning(
    show: Boolean,
    modifier: Modifier = Modifier
) {
    val flashingAnim = rememberInfiniteTransition()
    val alpha = flashingAnim.animateFloat(
        initialValue = Constants.ONE_F,
        targetValue = ContentAlpha.PERCENT_32,
        animationSpec = infiniteRepeatable(
            animation = tween(delayMillis = WARNING_FLASH_DELAY),
            repeatMode = RepeatMode.Reverse
        )
    )

    AnimatedVisibility(
        visible = show,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = stringResource(R.string.content_description_balance_low_warning),
            tint = Color.Yellow.copy(alpha = alpha.value),
            modifier = Modifier
                .size(SmallIconSize)
        )
    }
}

private const val WARNING_FLASH_DELAY = 1000

@Composable
private fun MonthlyStats(
    monthStats: List<MonthStats>,
    selectedMonth: String,
    onMonthSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = SpacingSmall),
        color = MaterialTheme.colorScheme.surfaceVariant
            .copy(alpha = ContentAlpha.PERCENT_16),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = MonthStatsMinHeight)
                .padding(vertical = SpacingSmall),
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
                        .matchParentSize(),
                    horizontalArrangement = Arrangement.spacedBy(SpacingSmall),
                    verticalAlignment = Alignment.Bottom,
                    contentPadding = PaddingValues(
                        start = SpacingMedium,
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

@Composable
private fun MonthBar(
    month: String,
    selected: Boolean,
    spentAmount: String,
    expenditurePercentage: Float,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var showExpenditureAmount by remember { mutableStateOf(false) }

    val barColor by animateColorAsState(
        targetValue = if (expenditurePercentage >= Constants.ONE_F) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.primary
    )
    val barIndicatorAlpha by animateFloatAsState(
        targetValue = when {
            showExpenditureAmount -> ContentAlpha.PERCENT_08
            selected -> Constants.ONE_F
            else -> ContentAlpha.PERCENT_16
        }
    )

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
                .combinedClickable(
                    onClick = { if (!selected) onClick() },
                    onLongClick = {
                        coroutineScope.launch {
                            showExpenditureAmount = true
                            delay(EXPENDITURE_AMOUNT_DISPLAY_DURATION)
                            showExpenditureAmount = false
                        }
                    }
                )
                .drawBehind {
                    val heightPercent = size.height * expenditurePercentage
                        .coerceAtMost(Constants.ONE_F)
                    drawRoundRect(
                        color = barColor,
                        cornerRadius = CornerRadius(STAT_BAR_CORNER_RADIUS),
                        topLeft = Offset(
                            x = 0f,
                            y = size.height - heightPercent
                        ),
                        alpha = barIndicatorAlpha
                    )
                },
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
                        .widthIn(min = MonthBarMinWidth)
                ) {
                    Text(
                        text = spentAmount,
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = SpacingXSmall, vertical = SpacingSmall)
                    )
                }
            }
            this@Column.AnimatedVisibility(visible = selected) {
                Text(
                    text = TextUtil.formatPercent(expenditurePercentage),
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColorFor(backgroundColor = barColor)
                )
            }
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
private fun TagFilters(
    tags: List<String>,
    selectedTag: String,
    onTagClick: (String) -> Unit,
    onTagLongClick: () -> Unit,
    onTagDelete: (String) -> Unit,
    tagDeletionModeActive: Boolean
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(end = ListPaddingLarge),
        horizontalArrangement = Arrangement.spacedBy(SpacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (tags.isNotEmpty() && !tagDeletionModeActive) {
            item(key = Constants.STRING_ALL) {
                FilterChip(
                    selected = selectedTag.isEmpty(),
                    onClick = { onTagClick(Constants.STRING_ALL) },
                    label = { Text(Constants.STRING_ALL) },
                    modifier = Modifier
                        .animateItemPlacement()
                )
            }
        }
        items(items = tags, key = { it }) { tag ->
            TagItem(
                selected = tag == selectedTag,
                label = tag,
                onClick = { onTagClick(tag) },
                onLongClick = onTagLongClick,
                deleteModeActive = tagDeletionModeActive,
                onDelete = { onTagDelete(tag) },
                modifier = Modifier
                    .animateItemPlacement()
            )
        }
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
            .padding(vertical = SpacingSmall)
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
                .padding(horizontal = SpacingSmall)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(horizontal = SpacingSmall)
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
    onLongClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.primaryContainer.copy(alpha = ContentAlpha.PERCENT_16)
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(SpacingMedium)
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

private val MonthStatsMinHeight = 120.dp
private val MonthBarMinWidth = 40.dp
private const val STAT_BAR_CORNER_RADIUS = 16f
private const val EXPENDITURE_AMOUNT_DISPLAY_DURATION = 5000L

@Preview(showBackground = true)
@Composable
private fun PreviewScreenContent() {
    XpenseTrackerTheme {
        ExpenseListScreenContent(
            state = ExpensesState(
                tags = listOf("Bills", "Leisure"),
                selectedTag = Constants.STRING_ALL,
                monthsToExpenditurePercents = emptyList(), /*(1..5).map {
                    MonthAndExpenditurePercent("Month $it", it * 0.1f)
                }*/
                selectedMonth = "Month 1",
                expenses = (1..20).map {
                    ExpenseListItem(
                        id = it.toLong(),
                        name = "Expense $it",
                        amount = "${it * 100}",
                        date = "Monday, 10th"
                    )
                },
            ),
            snackbarController = rememberSnackbarController(),
            actions = object : ExpensesActions {
                override fun onTagFilterSelect(tag: String) {}
                override fun onTagLongClick() {}
                override fun onTagDeleteClick(tag: String) {}
                override fun onDeleteExpensesWithTagDismiss() {}
                override fun onDeleteExpensesWithTagConfirm() {}
                override fun onAddFabClick() {}
                override fun onMonthSelect(month: String) {}
                override fun onExpenseClick(id: Long) {}
                override fun onExpenseLongClick(id: Long) {}
                override fun onCancelMultiSelectionMode() {}
                override fun onSelectOrDeselectAllOptionClick(isAllSelected: Boolean) {}
                override fun onDeleteOptionClick() {}
                override fun onDeleteExpensesDismiss() {}
                override fun onDeleteExpensesConfirm() {}
            },
            navigateToBottomBarDestination = {},
            navigateToLimitUpdate = {}
        )
    }
}