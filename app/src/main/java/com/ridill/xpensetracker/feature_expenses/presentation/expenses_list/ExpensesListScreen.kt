package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.SnackbarController
import com.ridill.xpensetracker.core.ui.components.XTSnackbarHost
import com.ridill.xpensetracker.core.ui.components.rememberSnackbarController
import com.ridill.xpensetracker.core.ui.theme.*
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.core.ui.util.slideInHorizontallyWithFadeIn
import com.ridill.xpensetracker.core.ui.util.slideOutHorizontallyWithFadeOut
import com.ridill.xpensetracker.core.util.Constants
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseListItem
import com.ridill.xpensetracker.feature_expenses.domain.model.MonthAndExpenditurePercent
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ADD_EDIT_EXPENSE_RESULT
import kotlin.math.roundToInt

@Composable
fun ExpensesListScreen(
    navController: NavController
) {
    val viewModel: ExpensesViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(ExpensesState.INITIAL)
    val context = LocalContext.current
    val snackbarController = rememberSnackbarController()
    val hapticFeedback = LocalHapticFeedback.current
    val currentBackStackEntry = navController.currentBackStackEntry

    // Add/Edit Expense Result
    val addEditExpenseResult = currentBackStackEntry
        ?.savedStateHandle?.getLiveData<String>(ADD_EDIT_EXPENSE_RESULT)?.observeAsState()
    LaunchedEffect(addEditExpenseResult) {
        currentBackStackEntry?.savedStateHandle
            ?.remove<String>(ADD_EDIT_EXPENSE_RESULT)

        addEditExpenseResult?.value?.let(viewModel::onAddEditResult)
    }

    // Collect Events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ExpensesViewModel.ExpenseListEvent.Navigate -> {
                    navController.navigate(event.route)
                }
                is ExpensesViewModel.ExpenseListEvent.ShowSnackbar -> {
                    snackbarController.showSnackbar(event.message.asString(context))
                }
            }.exhaustive
        }
    }

    ScreenContent(
        state = state,
        snackbarController = snackbarController,
        actions = viewModel
    )
}

@Composable
private fun ScreenContent(
    state: ExpensesState,
    snackbarController: SnackbarController,
    actions: ExpensesActions,
) {
    val topBarScrollState = rememberTopAppBarScrollState()
    val topAppBarScrollBehavior = remember {
        TopAppBarDefaults.enterAlwaysScrollBehavior(topBarScrollState)
    }

    Scaffold(
        snackbarHost = { XTSnackbarHost(snackbarController) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        bottomBar = {
            BottomAppBar(
                icons = {

                },
                floatingActionButton = {
                    AnimatedVisibility(visible = state.expenditureLimit > 0L) {
                        FloatingActionButton(
                            onClick = actions::onAddFabClick
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = stringResource(R.string.content_description_add_expense)
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
                    .copy(alpha = ContentAlpha.PERCENT_08)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GreetingAndLimit(
                limit = state.expenditureLimit,
                onLimitUpdate = actions::onExpenditureLimitUpdateClick
            )
            MonthsBarsRow(
                months = state.monthsToExpenditurePercents,
                selectedMonth = state.selectedMonth,
                onMonthSelect = actions::onMonthSelect,
                modifier = Modifier
                    .fillMaxHeight(MonthBarsHeightPercent)
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
                                selected = state.selectedTag == Constants.STRING_ALL,
                                onClick = { actions.onTagFilterSelect(Constants.STRING_ALL) },
                                label = { Text(Constants.STRING_ALL) }
                            )
                        }
                    }
                    items(state.tags) { tag ->
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(SpacingSmall),
                contentPadding = PaddingValues(
                    bottom = ListPaddingLarge,
                    start = PaddingMedium,
                    end = PaddingMedium
                ),
                modifier = Modifier
                    .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.destination_expenses),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .paddingFromBaseline(
                                top = PaddingMedium,
                                bottom = PaddingSmall
                            )
                    )
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

        if (state.showExpenditureLimitUpdateDialog) {
            ExpenditureLimitUpdateDialog(
                previousLimit = TextUtil.formatAmountWithCurrency(state.expenditureLimit),
                onDismiss = actions::onExpenditureLimitUpdateDialogDismiss,
                onConfirm = actions::onExpenditureLimitUpdateDialogConfirm
            )
        }

        if (state.showTagDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = actions::onDeleteExpensesWithTagDismiss,
                confirmButton = {
                    Button(onClick = actions::onDeleteExpensesWithTagConfirm) {
                        Text(stringResource(R.string.action_confirm))
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = actions::onDeleteExpensesWithTagDismiss) {
                        Text(stringResource(R.string.action_cancel))
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.DeleteForever,
                        contentDescription = null
                    )
                },
                title = { Text(stringResource(R.string.delete_tagged_expenses_question)) },
                text = { Text(stringResource(R.string.delete_tagged_expenses_message)) }
            )
        }
    }
}

@Composable
private fun GreetingAndLimit(
    limit: Long,
    onLimitUpdate: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(PaddingMedium)
    ) {
        Text(
            text = stringResource(R.string.greeting),
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
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(R.string.expenditure_limit_label),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(SpacingSmall))
                Text(
                    text = TextUtil.formatAmountWithCurrency(limit),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(SpacingSmall))
                IconButton(
                    onClick = onLimitUpdate,
                    modifier = Modifier
                        .size(SmallIconSize)
                        .align(Alignment.Top)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = stringResource(R.string.content_description_edit_limit),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthsBarsRow(
    months: List<MonthAndExpenditurePercent>,
    selectedMonth: String,
    onMonthSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(PaddingMedium)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
                    .copy(alpha = ContentAlpha.PERCENT_16),
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        if (months.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bar_data),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(SpacingSmall))
                Text(
                    text = stringResource(R.string.error_no_data_yet),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .matchParentSize(),
                horizontalArrangement = Arrangement.spacedBy(SpacingSmall),
                verticalAlignment = Alignment.Bottom,
                contentPadding = PaddingValues(
                    start = PaddingMedium,
                    end = PaddingLarge,
                    top = PaddingSmall,
                    bottom = PaddingMedium
                )
            ) {
                items(months) { (month, expenditurePercent) ->
                    MonthBar(
                        month = month,
                        selected = month == selectedMonth,
                        expenditurePercentage = expenditurePercent,
                        onClick = { onMonthSelect(month) },
                        modifier = Modifier
                            .fillParentMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthBar(
    month: String,
    selected: Boolean,
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
            expenditurePercentage > Constants.ONE_F -> MaterialTheme.colorScheme.errorContainer
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .weight(WEIGHT_1),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(expenditurePercentage * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(SpacingExtraSmall))
            Box(
                modifier = Modifier
                    .width(MonthBarWidth)
                    .fillMaxHeight(expenditurePercentage.coerceAtMost(Constants.ONE_F))
                    .scale(scaleY = scale, scaleX = Constants.ONE_F)
                    .clip(MaterialTheme.shapes.small)
                    .background(color = color)
                    .clickable(onClick = onClick)
            )
        }
        Spacer(modifier = Modifier.height(SpacingExtraSmall))
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
            containerColor = MaterialTheme.colorScheme.surfaceVariant
                .copy(alpha = ContentAlpha.PERCENT_40),
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
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = amount,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
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

private const val MonthBarsHeightPercent = 0.32f
private val MonthBarWidth = 40.dp
private val SmallIconSize = 16.dp

@Preview(showBackground = true)
@Composable
private fun PreviewScreenContent() {
    XpenseTrackerTheme {
        ScreenContent(
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
            }
        )
    }
}