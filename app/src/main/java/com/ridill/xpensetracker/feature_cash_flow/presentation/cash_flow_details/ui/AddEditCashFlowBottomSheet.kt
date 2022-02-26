package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingMedium
import com.ridill.xpensetracker.core.ui.theme.SpacingSmall
import kotlin.math.roundToInt

@Composable
fun AddEditCashFlowBottomSheet(
    showRepaymentOption: Boolean,
    name: String,
    onNameChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    lending: Boolean,
    onLendingChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }
    var repaymentMode by remember { mutableStateOf(false) }
    var repaymentAmount by remember { mutableStateOf("") }
    val repaymentToggleRotation by animateFloatAsState(targetValue = if (repaymentMode) 180f else 0f)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(PaddingSmall),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.dismiss)
                    )
                }
                Spacer(modifier = Modifier.width(SpacingMedium))
                Text(
                    text = stringResource(R.string.cash_flow),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = { onConfirm(repaymentAmount) }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.confirm)
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = PaddingMedium)
                .padding(bottom = PaddingMedium)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                label = { Text(stringResource(R.string.description)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(SpacingMedium))
            TextField(
                value = amount, onValueChange = onAmountChange,
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(stringResource(R.string.amount)) },
                placeholder = { Text(amount) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onConfirm(repaymentAmount) }
                ),
                readOnly = repaymentMode,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(SpacingMedium))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                if (showRepaymentOption) {
                    OutlinedButton(
                        onClick = {
                            repaymentAmount = ""
                            repaymentMode = !repaymentMode
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        AnimatedVisibility(visible = !repaymentMode) {
                            Text(stringResource(R.string.repayment))
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.toggle_repayment),
                            modifier = Modifier
                                .rotate(repaymentToggleRotation)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(SpacingSmall))
                AnimatedContent(
                    targetState = repaymentMode,
                    transitionSpec = {
                        if (targetState) {
                            slideInHorizontally { -(it * 0.1f).roundToInt() } + fadeIn() with
                                    slideOutHorizontally { (it * 0.1f).roundToInt() } + fadeOut()
                        } else {
                            slideInHorizontally { (it * 0.1f).roundToInt() } + fadeIn() with
                                    slideOutHorizontally { -(it * 0.1f).roundToInt() } + fadeOut()
                        } using SizeTransform(false)
                    }
                ) { repaymentMode ->
                    if (repaymentMode) {
                        OutlinedTextField(
                            value = repaymentAmount,
                            onValueChange = { repaymentAmount = it.trim() },
                            modifier = Modifier
                                .width(TotalSliderWidth),
                            placeholder = { Text(stringResource(R.string.repaid_amount)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onConfirm(repaymentAmount) }
                            )
                        )
                    } else {
                        LendingSlider(
                            modifier = Modifier,
                            lending = lending,
                            onLendingChange = onLendingChange
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LendingSlider(
    modifier: Modifier = Modifier,
    lending: Boolean,
    onLendingChange: (Boolean) -> Unit
) {
    val swipeableState = rememberSwipeableState(
        initialValue = lending,
        confirmStateChange = { newValue ->
            onLendingChange(newValue)
            true
        }
    )
    val maxWidth =
        with(LocalDensity.current) { (TotalSliderWidth - (SliderSize + 16.dp)).toPx() }
    val anchors = mapOf(0f to true, maxWidth to false)

    Box(
        modifier = modifier
            .width(TotalSliderWidth)
            .height(TextFieldDefaults.MinHeight)
            .padding(vertical = PaddingSmall)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colors.primaryVariant)
            .padding(PaddingSmall)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
            ),
    ) {
        AnimatedContent(targetState = lending) { lent ->
            Text(
                text = stringResource(
                    if (lent) R.string.lending
                    else R.string.borrowing
                ),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        ) {
            Icon(
                imageVector = Icons.Default.CompareArrows,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
            )
        }
    }
}

private val SliderSize = 24.dp
private val TotalSliderWidth = SliderSize * 8
