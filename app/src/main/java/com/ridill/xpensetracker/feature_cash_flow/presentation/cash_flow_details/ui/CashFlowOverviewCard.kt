package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.SpacingMedium
import com.ridill.xpensetracker.core.ui.util.numberSliderTransition
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowStatus

@Composable
fun CashFlowOverviewCard(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    name: String,
    onNameChange: (String) -> Unit,
    aggregateAmount: String,
    cashFlowStatus: CashFlowStatus,
    onConfirmEdit: () -> Unit
) {
    val transition =
        updateTransition(
            targetState = isEditMode,
            label = "Background Gradient Transition"
        )
    val gradientStartColor by transition.animateColor(label = "Background Gradient Start Color") { editMode ->
        if (editMode) MaterialTheme.colors.surface
        else MaterialTheme.colors.primaryVariant.copy(alpha = 0.48f)
    }
    val gradientEndColor by transition.animateColor(label = "Background Gradient End Color") { editMode ->
        if (editMode) MaterialTheme.colors.onSurface.copy(alpha = 0.24f)
        else MaterialTheme.colors.primary
    }
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(isEditMode) {
        if (isEditMode) focusRequester.requestFocus()
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            gradientStartColor,
                            gradientEndColor
                        ),
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(PaddingMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AnimatedContent(
                targetState = isEditMode,
                modifier = Modifier
                    .weight(1f)
            ) { editMode ->
                if (editMode) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        label = { Text(stringResource(R.string.person_name)) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { onConfirmEdit() }
                        ),
                        trailingIcon = {
                            IconButton(onClick = onConfirmEdit) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = stringResource(R.string.done),
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        },
                    )
                } else {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
            Spacer(modifier = Modifier.width(SpacingMedium))
            AnimatedVisibility(visible = !isEditMode) {
                AnimatedContent(targetState = cashFlowStatus) { status ->
                    CashFlowStatusIndicator(
                        amount = aggregateAmount,
                        statusMessage = status.label,
                        cleared = status == CashFlowStatus.CLEARED
                    )
                }
            }
        }
    }
}

@Composable
private fun CashFlowStatusIndicator(
    amount: String,
    @StringRes statusMessage: Int,
    cleared: Boolean
) {
    Column {
        AnimatedVisibility(visible = !cleared) {
            AnimatedContent(
                targetState = amount,
                transitionSpec = { numberSliderTransition { targetState > initialState } }
            ) { amount ->
                Text(
                    text = amount,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(statusMessage),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary
            )
            AnimatedVisibility(visible = cleared) {
                Spacer(modifier = Modifier.width(SpacingMedium))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
        }
    }
}