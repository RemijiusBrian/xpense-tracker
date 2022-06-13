package com.xpenses.android.feature_expenses.presentation.add_edit_expense

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.xpenses.android.R
import com.xpenses.android.core.ui.components.BackArrowButton
import com.xpenses.android.core.ui.components.SnackbarController
import com.xpenses.android.core.ui.components.XTSnackbarHost
import com.xpenses.android.core.ui.components.rememberSnackbarController
import com.xpenses.android.core.ui.theme.*
import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.core.util.Constants
import com.xpenses.android.core.util.exhaustive

@Composable
fun AddEditExpenseScreen(navController: NavController) {
    val viewModel: AddEditExpenseViewModel = hiltViewModel()
    val amount by viewModel.amount.observeAsState("")
    val name by viewModel.name.observeAsState("")
    val state by viewModel.state.observeAsState(AddEditExpenseState.INITIAL)
    val newTagInput by viewModel.newTagInput.observeAsState("")

    val snackbarController = rememberSnackbarController()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(snackbarController, context) {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewModel.events.collect { event ->
            when (event) {
                AddEditExpenseViewModel.AddEditEvents.ExpenseCreated -> {
                    keyboardController?.hide()
                    navController.previousBackStackEntry?.savedStateHandle
                        ?.set(ADD_EDIT_EXPENSE_RESULT, RESULT_EXPENSE_ADDED)
                    navController.popBackStack()
                }
                AddEditExpenseViewModel.AddEditEvents.ExpenseDeleted -> {
                    keyboardController?.hide()
                    navController.previousBackStackEntry?.savedStateHandle
                        ?.set(ADD_EDIT_EXPENSE_RESULT, RESULT_EXPENSE_DELETED)
                    navController.popBackStack()
                }
                AddEditExpenseViewModel.AddEditEvents.ExpenseUpdated -> {
                    keyboardController?.hide()
                    navController.previousBackStackEntry?.savedStateHandle
                        ?.set(ADD_EDIT_EXPENSE_RESULT, RESULT_EXPENSE_UPDATED)
                    navController.popBackStack()
                }
                is AddEditExpenseViewModel.AddEditEvents.ShowSnackbar -> {
                    snackbarController.showSnackbar(event.message.asString(context))
                }
            }.exhaustive
        }
    }

    ScreenContent(
        snackbarController = snackbarController,
        isEditMode = viewModel.editMode,
        amount = amount,
        name = name,
        newTagInput = newTagInput,
        state = state,
        actions = viewModel,
        navigateUp = navController::popBackStack
    )
}

@Composable
private fun ScreenContent(
    snackbarController: SnackbarController,
    isEditMode: Boolean,
    amount: String,
    name: String,
    newTagInput: String,
    state: AddEditExpenseState,
    actions: AddEditExpenseActions,
    navigateUp: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (!isEditMode) focusRequester.requestFocus()
    }

    Scaffold(
        snackbarHost = { XTSnackbarHost(snackbarController) },
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (isEditMode) R.string.edit_expense
                            else R.string.add_expense
                        )
                    )
                },
                navigationIcon = {
                    BackArrowButton(onClick = navigateUp)
                },
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = actions::onDeleteClick) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteForever,
                                contentDescription = stringResource(R.string.content_description_delete_expense)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = actions::onSave) {
                Icon(
                    imageVector = Icons.Rounded.Save,
                    contentDescription = stringResource(R.string.content_description_save)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = ListPaddingLarge,
                horizontal = PaddingMedium
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                AmountInput(
                    value = amount,
                    onValueChange = actions::onAmountChange,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                )
            }
            item {
                TextField(
                    value = name,
                    onValueChange = actions::onNameChange,
                    shape = MaterialTheme.shapes.medium,
                    textStyle = TextStyle.Default.copy(
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .defaultMinSize(minWidth = InputFieldMinWidth),
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                            .copy(alpha = ContentAlpha.PERCENT_32)
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { actions.onSave() }
                    )
                )
                Spacer(Modifier.width(SpacingMedium))
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(stringResource(R.string.label_monthly_expense_marker))
                    Checkbox(
                        checked = state.expense?.monthly ?: false,
                        onCheckedChange = actions::onMonthlyCheckChange
                    )
                }
            }
            item {
                Text(
                    text = stringResource(R.string.label_tag),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
//                        .align(Alignment.Start)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    mainAxisSpacing = SpacingSmall
                ) {
                    state.tagsList.forEach { tag ->
                        FilterChip(selected = tag == state.expense?.tag,
                            onClick = { actions.onTagSelect(tag) },
                            label = { Text(tag) }
                        )
                    }
                }
                AddNewTagChip(
                    onNewTagClick = actions::onNewTagClick,
                    tagInputExpanded = state.tagInputExpanded,
                    tagInput = newTagInput,
                    onInputChange = actions::onNewTagValueChange,
                    onDismiss = actions::onNewTagInputDismiss,
                    onConfirmInput = actions::onNewTagConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
//                        .align(Alignment.Start)
                )
            }
        }

        if (state.showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = actions::onDeleteDismiss,
                confirmButton = {
                    Button(onClick = actions::onDeleteConfirm) {
                        Text(stringResource(R.string.action_confirm))
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = actions::onDeleteDismiss) {
                        Text(stringResource(R.string.action_cancel))
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
                },
                title = {
                    Text(stringResource(R.string.delete_expense_question))
                },
                text = {
                    Text(stringResource(R.string.delete_expense_confirmation_message))
                }
            )
        }
    }
}


@Composable
private fun AmountInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.amount),
            style = MaterialTheme.typography.titleSmall
        )
        BasicTextField(
            value = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            ),
            onValueChange = { onValueChange(it.text) },
            modifier = Modifier
                .then(modifier),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            decorationBox = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = TextUtil.currencySymbol,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(SpacingExtraSmall))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                }
            }
        )
    }
}

@Composable
private fun AddNewTagChip(
    onNewTagClick: () -> Unit,
    tagInputExpanded: Boolean,
    tagInput: String,
    onInputChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirmInput: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        ElevatedAssistChip(
            onClick = onNewTagClick,
            label = { Text(stringResource(R.string.new_tag)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.content_description_tags)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        NewTagInput(
            expanded = tagInputExpanded,
            tagInput = tagInput,
            onInputChange = onInputChange,
            onDismiss = onDismiss,
            onConfirmInput = onConfirmInput
        )
    }
}

@Composable
private fun NewTagInput(
    expanded: Boolean,
    tagInput: String,
    onInputChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirmInput: () -> Unit
) {
    AnimatedVisibility(
        visible = expanded,
        enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)),
        exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f))
    ) {
        Surface(
            shadowElevation = Elevation4dp,
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .padding(PaddingMedium)
        ) {
            Box(
                modifier = Modifier
                    .padding(PaddingSmall)
            ) {
                TextField(
                    value = tagInput,
                    onValueChange = {
                        if (it.length <= Constants.TAG_NAME_MAX_LENGTH) onInputChange(it)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_tags),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Rounded.Cancel,
                                    contentDescription = stringResource(R.string.action_cancel)
                                )
                            }

                            IconButton(onClick = onConfirmInput) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = stringResource(R.string.action_confirm)
                                )
                            }
                        }
                    },
                    label = { Text(stringResource(R.string.enter_tag)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onConfirmInput() }
                    ),
                    placeholder = {
                        Text(stringResource(R.string.max_chars, Constants.TAG_NAME_MAX_LENGTH))
                    }
                )
            }
        }
    }
}

private val InputFieldMinWidth = 40.dp

@Preview(showBackground = true)
@Composable
private fun PreviewScreenContent() {
    XpenseTrackerTheme {
        ScreenContent(
            isEditMode = true,
            amount = "100",
            name = "",
            state = AddEditExpenseState(
                tagsList = (1..10).map { "Tag $it" }
            ),
            actions = object :
                AddEditExpenseActions {
                override fun onAmountChange(value: String) {}
                override fun onNameChange(value: String) {}
                override fun onMonthlyCheckChange(isChecked: Boolean) {}
                override fun onTagSelect(tag: String) {}
                override fun onNewTagClick() {}
                override fun onNewTagValueChange(value: String) {}
                override fun onNewTagInputDismiss() {}
                override fun onNewTagConfirm() {}
                override fun onDeleteClick() {}
                override fun onDeleteDismiss() {}
                override fun onDeleteConfirm() {}
                override fun onSave() {}
            },
            navigateUp = {},
            snackbarController = rememberSnackbarController(),
            newTagInput = ""
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTagInput() {
    XpenseTrackerTheme {
        NewTagInput(
            expanded = true,
            tagInput = "Name",
            onInputChange = {},
            onDismiss = {},
            onConfirmInput = {}
        )
    }
}