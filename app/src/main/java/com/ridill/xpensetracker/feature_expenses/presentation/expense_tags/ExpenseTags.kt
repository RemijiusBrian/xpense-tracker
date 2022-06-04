package com.ridill.xpensetracker.feature_expenses.presentation.expense_tags

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.BackArrowButton
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag

@Composable
fun ExpenseTags(navController: NavHostController) {

    val viewModel: TagsViewModel = hiltViewModel()
    val state by viewModel.tagsListState.observeAsState(TagsListState.INITIAL)

    ScreenContent(
        state = state,
        actions = viewModel,
        navigateBack = navController::popBackStack
    )
}

@Composable
private fun ScreenContent(
    state: TagsListState,
    actions: TagsActions,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Destination.ExpenseTags.label)) },
                navigationIcon = { BackArrowButton(onClick = navigateBack) }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(state.tagsWithExpense) { tagWithExpense ->
                TagItem(
                    tag = tagWithExpense.tag,
                    expenses = tagWithExpense.expenses,
                    expenseAmount = tagWithExpense.expenseAmountFormatted,
                    onOptionClick = {}
                )
            }
        }
    }
}

@Composable
private fun TagItem(
    modifier: Modifier = Modifier,
    tag: ExpenseTag,
    expenses: List<Expense>,
    expenseAmount: String,
    onOptionClick: (TagOptions) -> Unit
) {
    var showOptions by remember { mutableStateOf(false) }
    var showExpenses by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingMedium)
        ) {
            TagOptionsMenu(
                modifier = Modifier
                    .align(Alignment.End),
                showOptions = showOptions,
                onShowMenuClick = { showOptions = true },
                onDismiss = { showOptions = false },
                onOptionClick = onOptionClick,
                tag = tag
            )
            Text(
                text = tag.name,
                style = MaterialTheme.typography.h5,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = expenseAmount,
                style = MaterialTheme.typography.h6
            )

            if (showExpenses) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(
                            min = TextFieldDefaults.MinHeight,
                            max = TextFieldDefaults.MinHeight * 3
                        )
                ) {}
            }
        }
    }
}

@Composable
private fun TagOptionsMenu(
    modifier: Modifier = Modifier,
    tag: ExpenseTag,
    showOptions: Boolean,
    onShowMenuClick: () -> Unit,
    onDismiss: () -> Unit,
    onOptionClick: (TagOptions) -> Unit
) {
    Box(modifier = modifier) {
        IconButton(onClick = onShowMenuClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.options_menu)
            )
        }
        DropdownMenu(
            expanded = showOptions,
            onDismissRequest = onDismiss
        ) {
            DropdownMenuItem(onClick = { onOptionClick(TagOptions.EditTag(tag)) }) {
                Text(stringResource(R.string.edit))
            }

            DropdownMenuItem(onClick = { onOptionClick(TagOptions.DeleteTag(tag)) }) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}