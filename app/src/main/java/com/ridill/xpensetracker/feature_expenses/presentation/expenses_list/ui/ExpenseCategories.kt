package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ui

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.core.ui.theme.PaddingExtraSmall
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

@Composable
fun ExpenseCategories(
    modifier: Modifier = Modifier,
    categories: List<ExpenseCategory> = ExpenseCategory.values().toList(),
    selectedCategory: ExpenseCategory?,
    onCategorySelect: (ExpenseCategory) -> Unit
) {
    LazyRow(
        modifier = modifier,
    ) {
        items(categories) { category ->
            CategoryChip(
                label = category.label,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelect(category) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    @StringRes label: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.primary
        else MaterialTheme.colors.surface
    )
    Surface(
        modifier = Modifier
            .padding(PaddingExtraSmall),
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, color = MaterialTheme.colors.secondary),
        color = color,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(PaddingSmall)
        ) {
            Text(
                stringResource(label),
                style = MaterialTheme.typography.subtitle2,
            )
        }
    }
}