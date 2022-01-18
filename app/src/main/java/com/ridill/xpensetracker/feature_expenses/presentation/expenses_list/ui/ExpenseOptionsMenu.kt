package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.domain.model.SortOrder
import com.ridill.xpensetracker.core.presentation.ui.theme.SpacingMedium
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseMenuOption
import com.ridill.xpensetracker.feature_expenses.domain.model.SortCriteria

@Composable
fun ExpenseOptionsMenu(
    onOptionClick: (ExpenseMenuOption) -> Unit,
    showAll: Boolean,
    currentSortCriteria: SortCriteria,
    currentSortOrder: SortOrder
) {
    var expanded by remember { mutableStateOf(false) }
    var menuSelection by remember { mutableStateOf(MenuSelection.MAIN) }

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                menuSelection = MenuSelection.MAIN
            },
            modifier = Modifier
                .width(160.dp),
        ) {
            AnimatedContent(
                targetState = menuSelection,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { it } with slideOutHorizontally { -it }
                    } else {
                        slideInHorizontally { -it } with slideOutHorizontally { it }
                    } using SizeTransform(clip = false)
                }
            ) { selection ->
                Column {
                    when (selection) {
                        MenuSelection.MAIN -> {
                            MainOptions(
                                onSortOptionClick = { menuSelection = MenuSelection.SORT },
                                previousEntriesShown = showAll,
                                onShowPreviousExpensesClick = {
                                    expanded = false
                                    onOptionClick(ExpenseMenuOption.ShowAllEntries(it))
                                }
                            )
                        }
                        MenuSelection.SORT -> {
                            SortOptions(
                                selectedCriteria = currentSortCriteria,
                                selectedOrder = currentSortOrder,
                                onClick = { criteria, order ->
                                    expanded = false
                                    menuSelection = MenuSelection.MAIN
                                    onOptionClick(
                                        ExpenseMenuOption.SortOption(
                                            criteria = criteria,
                                            order = order
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SortOptions(
    selectedCriteria: SortCriteria,
    selectedOrder: SortOrder,
    onClick: (SortCriteria, SortOrder) -> Unit
) {
    SortCriteria.values().forEach { criteria ->
        val isSelected = criteria == selectedCriteria
        DropdownMenuItem(onClick = {
            onClick(
                criteria,
                if (isSelected) {
                    when (selectedOrder) {
                        SortOrder.ASCENDING -> SortOrder.DESCENDING
                        SortOrder.DESCENDING -> SortOrder.ASCENDING
                    }
                } else {
                    SortOrder.ASCENDING
                }
            )
        }) {
            Text(
                stringResource(criteria.label),
                modifier = Modifier
                    .weight(1f)
            )
            if (isSelected) {
                Icon(
                    imageVector = when (selectedOrder) {
                        SortOrder.ASCENDING -> Icons.Default.ArrowUpward
                        SortOrder.DESCENDING -> Icons.Default.ArrowDownward
                    },
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun MainOptions(
    onSortOptionClick: () -> Unit,
    previousEntriesShown: Boolean,
    onShowPreviousExpensesClick: (Boolean) -> Unit
) {
    DropdownMenuItem(onClick = onSortOptionClick) {
        Text(
            stringResource(R.string.sort),
            modifier = Modifier
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(SpacingMedium))
        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = null)
    }
    Divider()
    DropdownMenuItem(onClick = { onShowPreviousExpensesClick(!previousEntriesShown) }) {
        Text(
            stringResource(R.string.show_all_entries),
            modifier = Modifier
                .weight(1f)
        )
        Checkbox(checked = previousEntriesShown, onCheckedChange = onShowPreviousExpensesClick)
    }
}

enum class MenuSelection { MAIN, SORT }