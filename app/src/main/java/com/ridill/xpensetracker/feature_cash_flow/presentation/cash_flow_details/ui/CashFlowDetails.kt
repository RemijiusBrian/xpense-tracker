package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.BackArrowButton
import com.ridill.xpensetracker.core.ui.components.ConfirmationDialog
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.theme.*
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowDetailsOptions
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.CASH_FLOW_RESULT
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.CashFlowDetailsActions
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.CashFlowDetailsState
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.CashFlowDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CashFlowDetails(
    navController: NavController,
    viewModel: CashFlowDetailsViewModel = hiltViewModel()
) {
    val expenseName by viewModel.expenseName.observeAsState("")
    val state by viewModel.state.observeAsState(CashFlowDetailsState.initial)
    val activeCashFlow by viewModel.activeCashFlow.observeAsState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current

    // BackPress when bottom sheet open
    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    LaunchedEffect(Unit) {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewModel.events.collectLatest { event ->
            when (event) {
                is CashFlowDetailsViewModel.CashFlowDetailsEvents.ShowSnackbar -> {
                    bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                        context.getString(event.message)
                    )
                }
                is CashFlowDetailsViewModel.CashFlowDetailsEvents.ToggleAddEditCashFlow -> {
                    if (event.show) {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        } else Unit
                    } else {
                        keyboardController?.hide()
                        if (bottomSheetScaffoldState.bottomSheetState.isExpanded)
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        else Unit
                    }
                }
                is CashFlowDetailsViewModel.CashFlowDetailsEvents.ShowCashFlowDeleteUndo -> {
                    val snackbarResult = bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.cash_flow_deleted),
                        actionLabel = context.getString(R.string.undo)
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        viewModel.onUndoCashFlowDelete(event.cashFlow)
                    } else Unit
                }
                is CashFlowDetailsViewModel.CashFlowDetailsEvents.NavigateBackWithResult -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        CASH_FLOW_RESULT, event.result
                    )
                    navController.popBackStack()
                }
                CashFlowDetailsViewModel.CashFlowDetailsEvents.NavigateBack -> {
                    navController.popBackStack()
                }
                is CashFlowDetailsViewModel.CashFlowDetailsEvents.ProvideHapticFeedback -> {
                    hapticFeedback.performHapticFeedback(event.feedbackType)
                }
            }.exhaustive
        }
    }

    ScreenContent(
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        state = state,
        expenseName = expenseName,
        actions = viewModel,
        activeCashFlow = activeCashFlow,
        navigateBack = navController::popBackStack
    )
}

@Composable
private fun ScreenContent(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: CashFlowDetailsState,
    expenseName: String,
    actions: CashFlowDetailsActions,
    activeCashFlow: CashFlow?,
    navigateBack: () -> Unit
) {
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            activeCashFlow?.let { cashFlow ->
                AddEditCashFlowBottomSheet(
                    name = cashFlow.name,
                    onNameChange = actions::onCashFlowNameChange,
                    amount = cashFlow.amount.takeIf { it > 0 }?.toString().orEmpty(),
                    onAmountChange = actions::onCashFlowAmountChange,
                    lending = cashFlow.lending,
                    onLendingChange = actions::onCashFlowLendingChange,
                    onDismiss = actions::onDismissAddEditCashFlow,
                    onConfirm = actions::onAddEditCashFlowConfirm
                )
            }
        },
        sheetPeekHeight = ZeroDp,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Destination.CashFlowDetails.label)) },
                navigationIcon = {
                    AnimatedContent(targetState = state.editMode) { editMode ->
                        if (editMode) {
                            IconButton(onClick = actions::onDismissEditMode) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null)
                            }
                        } else {
                            BackArrowButton(navigateBack)
                        }
                    }
                },
                actions = {
                    AnimatedVisibility(visible = !state.editMode) {
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = stringResource(R.string.options_menu)
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                CashFlowDetailsOptions.values().forEach { option ->
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        actions.onMenuOptionSelect(option)
                                    }) {
                                        Text(stringResource(option.label))
                                    }
                                }
                            }
                        }
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = PaddingMedium)
                .padding(top = PaddingLarge)
        ) {
            state.cashFlowStatus?.let { it1 ->
                CashFlowOverviewCard(
                    isEditMode = state.editMode,
                    name = expenseName,
                    onNameChange = actions::onExpenseNameChange,
                    aggregateAmount = state.aggregateAmount,
                    onConfirmEdit = actions::onConfirmExpenseNameChange,
                    cashFlowStatus = it1
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = IconButtonSize),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.cash_flow),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
                AnimatedVisibility(visible = state.showAddCashFlowButton) {
                    IconButton(onClick = actions::onAddCashFlowClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_cash_flow),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
            Surface(
                elevation = ElevationSmall,
                shape = MaterialTheme.shapes.medium.copy(
                    bottomStart = CornerSize(ZeroDp),
                    bottomEnd = CornerSize(ZeroDp),
                ),
                color = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(state.cashFlow, key = { it.id }) { cashFlow ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = { dismissValue ->
                                if (dismissValue == DismissValue.DismissedToEnd || dismissValue == DismissValue.DismissedToStart) {
                                    actions.onCashFlowSwipeDelete(cashFlow)
                                }
                                true
                            }
                        )
                        SwipeToDismiss(
                            state = dismissState,
                            background = {},
                            modifier = Modifier
                                .animateItemPlacement()
                        ) {
                            CashFlowItem(
                                modifier = Modifier,
                                name = cashFlow.name,
                                amount = cashFlow.amountFormatted,
                                date = cashFlow.dateFormatted,
                                lending = cashFlow.lending,
                                onClick = { actions.onCashFlowClick(cashFlow) }
                            )
                        }
                    }
                }
            }
        }
        if (state.showClearCashFlowConfirmation) {
            ConfirmationDialog(
                title = stringResource(R.string.confirm_cash_flow_strike_off),
                text = stringResource(
                    R.string.confirm_action_message,
                    stringResource(R.string.strike_this_cash_flow_off)
                ),
                onDismiss = actions::onStrikeOffDismiss,
                onConfirm = actions::onStrikeOffConfirm
            )
        }
    }
}