package com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.ConfirmationDialog
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ADD_EDIT_EXPENSE_RESULT
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.AddEditExpenseActions
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.AddEditExpenseViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditExpense(
    viewModel: AddEditExpenseViewModel = hiltViewModel(),
    navController: NavController
) {
    val expense by viewModel.expense.observeAsState(Expense.DEFAULT)
    val showDeleteExpenseConfirmation by viewModel.showDeleteExpenseDialog.observeAsState(false)
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    // Collect Events
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AddEditExpenseViewModel.AddEditEvents.NavigateBackWithResult -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        ADD_EDIT_EXPENSE_RESULT, event.result
                    )
                    navController.popBackStack()
                }
                is AddEditExpenseViewModel.AddEditEvents.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(event.message)
                    )
                }
            }.exhaustive
        }
    }

    ScreenContent(
        expense = expense,
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
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
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

            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = expense.amount.takeIf { it > 0L }?.toString().orEmpty(),
                onValueChange = actions::onAmountChange,
                modifier = Modifier
                    .fillMaxWidth(0.50f)
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
            Spacer(modifier = Modifier.height(12.dp))
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
}