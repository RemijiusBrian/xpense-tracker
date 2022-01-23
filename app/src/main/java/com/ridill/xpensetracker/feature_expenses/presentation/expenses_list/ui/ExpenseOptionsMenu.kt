package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseMenuOption

@Composable
fun ExpenseOptionsMenu(
    onOptionClick: (ExpenseMenuOption) -> Unit,
    showAll: Boolean,
) {
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
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(160.dp),
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onOptionClick(ExpenseMenuOption.ShowAllEntries(!showAll))
                }
            ) {
                Text(
                    stringResource(R.string.show_all_entries),
                    modifier = Modifier
                        .weight(1f)
                )
                Checkbox(
                    checked = showAll,
                    onCheckedChange = {
                        expanded = false
                        onOptionClick(ExpenseMenuOption.ShowAllEntries(it))
                    }
                )
            }
        }
    }
}