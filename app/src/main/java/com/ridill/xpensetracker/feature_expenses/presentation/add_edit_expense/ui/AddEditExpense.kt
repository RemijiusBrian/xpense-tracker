package com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.BackArrowButton
import com.ridill.xpensetracker.core.ui.components.ConfirmationDialog
import com.ridill.xpensetracker.core.ui.components.InputDialog
import com.ridill.xpensetracker.core.ui.theme.PaddingExtraSmall
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.SpacingMedium
import com.ridill.xpensetracker.core.ui.util.rememberSnackbarController
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ADD_EDIT_EXPENSE_RESULT
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.AddEditExpenseActions
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.AddEditExpenseViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditExpense(
    viewModel: AddEditExpenseViewModel = hiltViewModel(),
    navController: NavController
) {
    val expense by viewModel.expenseLiveData.observeAsState(Expense.DEFAULT)
    val tags by viewModel.tags.observeAsState(emptyList())
    val showNewTagDialog by viewModel.showNewTagDialog.observeAsState(false)
    val showDeleteExpenseConfirmation by viewModel.showDeleteExpenseDialog.observeAsState(false)
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarController = rememberSnackbarController(coroutineScope)

    // Collect Events
    LaunchedEffect(Unit) {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewModel.events.collectLatest { event ->
            when (event) {
                is AddEditExpenseViewModel.AddEditExpenseEvents.NavigateBackWithResult -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        ADD_EDIT_EXPENSE_RESULT, event.result
                    )
                    navController.popBackStack()
                }
                is AddEditExpenseViewModel.AddEditExpenseEvents.ShowSnackbar -> {
                    snackbarController.showSnackbar(
                        scaffoldState,
                        context.getString(event.message)
                    )
                }
            }.exhaustive
        }
    }

    ScreenContent(
        expense = expense,
        tags = tags,
        showNewTagDialog = showNewTagDialog,
        isEditMode = viewModel.isEditMode,
        showDeleteConfirmation = showDeleteExpenseConfirmation,
        scaffoldState = scaffoldState,
        actions = viewModel,
        navigateBack = navController::popBackStack
    )
}

@Composable
private fun ScreenContent(
    expense: Expense,
    tags: List<ExpenseTag>,
    showNewTagDialog: Boolean,
    isEditMode: Boolean,
    showDeleteConfirmation: Boolean,
    scaffoldState: ScaffoldState,
    actions: AddEditExpenseActions,
    navigateBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (isEditMode) R.string.edit_expense
                            else R.string.add_expense
                        )
                    )
                },
                navigationIcon = { BackArrowButton(onClick = navigateBack) },
                actions = {
                    if (isEditMode) {
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = stringResource(R.string.options_menu)
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        actions.onDeleteOptionClick()
                                    }
                                ) {
                                    Text(stringResource(R.string.delete))
                                }
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = actions::onSaveClick) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(R.string.save)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = PaddingMedium)
        ) {
            Spacer(modifier = Modifier.height(SpacingMedium))
            OutlinedTextField(
                value = expense.name,
                onValueChange = actions::onNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                label = { Text(stringResource(R.string.expense_name)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )
            LaunchedEffect(Unit) {
                if (!isEditMode) focusRequester.requestFocus()
            }

            Spacer(modifier = Modifier.height(SpacingMedium))
            TextField(
                value = expense.amount.takeIf { it > 0L }?.toString().orEmpty(),
                onValueChange = actions::onAmountChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End),
                label = { Text(stringResource(R.string.amount)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { actions.onSaveClick() }
                )
            )
            Spacer(modifier = Modifier.height(SpacingMedium))
            Row(
                modifier = Modifier
                    .align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.repeat_every_month))
                Checkbox(
                    checked = expense.isMonthly,
                    onCheckedChange = actions::onRepeatEveryMonthToggle
                )
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                tags.forEach { tag ->
                    TagListItem(
                        name = tag.name,
                        onClick = { actions.onTagSelect(tag.name) },
                        selected = tag.name == expense.tag
                    )
                }
            }
            AddTagChip(onClick = actions::onNewTagClick)
        }
    }

    if (showDeleteConfirmation) {
        ConfirmationDialog(
            text = stringResource(
                R.string.confirm_action_message,
                stringResource(R.string.delete_this_expense)
            ),
            title = stringResource(R.string.confirm_delete_expense_action),
            onDismiss = actions::onDeleteDialogDismissed,
            onConfirm = actions::onDeleteDialogConfirmed
        )
    }

    if (showNewTagDialog) {
        InputDialog(
            message = R.string.add_tag,
            onDismiss = actions::onNewTagDismiss,
            onConfirm = actions::onNewTagConfirm
        )
    }
}

@Composable
private fun TagListItem(
    modifier: Modifier = Modifier,
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected, onClick = onClick,
        modifier = modifier
            .padding(PaddingExtraSmall),
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = MaterialTheme.colors.primary,
            selectedContentColor = MaterialTheme.colors.onPrimary
        )
    ) { Text(name) }
}

@Composable
private fun AddTagChip(
    onClick: () -> Unit,
) {
    Chip(
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_tag)
            )
        }
    ) {
        Text(stringResource(R.string.add_tag))
    }
}