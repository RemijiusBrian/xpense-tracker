package dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
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
import com.google.accompanist.flowlayout.FlowRow
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.*
import dev.ridill.xpensetracker.core.ui.theme.*
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.util.Constants

@Composable
fun AddEditExpenseScreenContent(
    snackbarController: SnackbarController,
    isEditMode: Boolean,
    amount: String,
    name: String,
    newTagInput: String,
    state: AddEditExpenseState,
    actions: AddEditExpenseActions,
    navigateUp: () -> Unit
) {
    Scaffold(
        snackbarHost = { XTSnackbarHost(snackbarController) },
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        stringResource(
                            when {
                                state.isBillExpense -> R.string.bill_payment
                                isEditMode -> R.string.edit_expense
                                else -> R.string.add_expense
                            }
                        )
                    )
                },
                navigationIcon = { BackArrowButton(onClick = navigateUp) },
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
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = ListPaddingLarge,
                horizontal = SpacingMedium
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                AmountInput(
                    value = amount,
                    onValueChange = actions::onAmountChange,
                    readOnly = state.isBillExpense
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
                    ),
                    readOnly = state.isBillExpense,
                    placeholder = { Text(stringResource(R.string.add_note)) },
                )
            }
            if (!state.isBillExpense) {
                item {
                    Text(
                        text = stringResource(R.string.label_tag),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        mainAxisSpacing = SpacingSmall
                    ) {
                        state.tagsList.forEach { tag ->
                            FilterChip(selected = tag == state.selectedTag,
                                onClick = { actions.onTagSelect(tag) },
                                label = { Text(tag) }
                            )
                        }
                        ElevatedAssistChip(
                            onClick = actions::onNewTagClick,
                            label = { Text(stringResource(R.string.new_tag)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (state.tagInputExpanded) Icons.Default.Close
                                    else Icons.Default.Add,
                                    contentDescription = stringResource(R.string.content_description_create_new_tag)
                                )
                            },
                            colors = AssistChipDefaults.elevatedAssistChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                    NewTagInput(
                        expanded = state.tagInputExpanded,
                        tagInput = newTagInput,
                        onInputChange = actions::onNewTagValueChange,
                        onDismiss = actions::onNewTagInputDismiss,
                        onConfirmInput = actions::onNewTagConfirm
                    )
                }
            }
        }

        if (state.showDeleteConfirmation) {
            SimpleConfirmationDialog(
                title = R.string.delete_expense_title,
                text = R.string.delete_expense_confirmation_message,
                onDismiss = actions::onDeleteDismiss,
                onConfirm = actions::onDeleteConfirm,
                icon = Icons.Rounded.DeleteForever
            )
        }
    }
}


@Composable
private fun AmountInput(
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.displayMedium.copy(
        color = MaterialTheme.colorScheme.onBackground
    )
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
            modifier = modifier,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            readOnly = readOnly,
            textStyle = textStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                ) {
                    Text(
                        text = TextUtil.currencySymbol,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(SpacingXSmall))
                    Box {
                        this@Row.AnimatedVisibility(visible = value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.amount_placeholder),
                                style = textStyle,
                                color = MaterialTheme.colorScheme.onBackground
                                    .copy(alpha = ContentAlpha.PERCENT_16)
                            )
                        }
                        innerTextField()
                    }
                }
            }
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
            shadowElevation = ElevationSmall,
            tonalElevation = ElevationSmall,
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .padding(SpacingMedium)
        ) {
            Box(
                modifier = Modifier
                    .padding(SpacingSmall)
            ) {
                TextField(
                    value = tagInput,
                    onValueChange = onInputChange,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_tags),
                            contentDescription = stringResource(R.string.content_description_tags)
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

private val InputFieldMinWidth = 80.dp

@Preview(showBackground = true)
@Composable
private fun PreviewScreenContent() {
    XpenseTrackerTheme {
        AddEditExpenseScreenContent(
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