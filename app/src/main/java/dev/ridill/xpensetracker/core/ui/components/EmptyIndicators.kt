package dev.ridill.xpensetracker.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.theme.SpacingXXSmall
import dev.ridill.xpensetracker.core.ui.theme.SpacingSmall
import dev.ridill.xpensetracker.core.ui.theme.SpacingXSmall
import kotlinx.coroutines.delay

@Composable
fun ListEmptyIndicator(
    modifier: Modifier = Modifier,
    @StringRes message: Int = R.string.default_no_data_message
) {
    val animationValues = (1..3).map { index ->
        var offset by remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            delay(DELAY * index)
            animate(
                initialValue = 0f,
                targetValue = -12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = DURATION,
                        delayMillis = INTERVAL_DURATION
                    ),
                    repeatMode = RepeatMode.Reverse,
                )
            ) { animatedValue, _ -> offset = animatedValue }
        }
        offset
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        animationValues.forEach { animationValue ->
            ListBar(offset = animationValue.dp)
            Spacer(modifier = Modifier.height(SpacingXSmall))
        }
        Spacer(modifier = Modifier.height(SpacingXSmall))
        Text(
            text = stringResource(message),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ListBar(
    offset: Dp
) {
    Box(
        modifier = Modifier
            .offset(y = offset)
            .width(ListBarWidth)
            .height(ListBarHeight)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraSmall
            )
    )
}

@Composable
fun GridEmptyIndicator(
    modifier: Modifier = Modifier,
    @StringRes message: Int = R.string.default_no_data_message
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val animationValues = (1..4).map { index ->
            var offset by remember { mutableStateOf(0f) }

            LaunchedEffect(Unit) {
                delay(DELAY * index)
                animate(
                    initialValue = 0f,
                    targetValue = 6f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = DURATION,
                            delayMillis = INTERVAL_DURATION
                        ),
                        repeatMode = RepeatMode.Reverse,
                    )
                ) { animatedValue, _ -> offset = animatedValue }
            }
            offset
        }
        Box(
            modifier = Modifier
                .padding(SpacingXXSmall)
                .size(GridContainerSize)
        ) {
            animationValues.forEachIndexed { index, value ->
                val alignment = when (index) {
                    0 -> Alignment.TopStart
                    1 -> Alignment.TopEnd
                    2 -> Alignment.BottomStart
                    3 -> Alignment.BottomEnd
                    else -> Alignment.Center
                }
                GridSquare(
                    xOffset = value.dp * (if (index % 2 == 0) -1 else 1),
                    yOffset = value.dp * (if (index in (0..1)) -1 else 1),
                    modifier = Modifier
                        .align(alignment)
                )
            }
        }
        Spacer(modifier = Modifier.height(SpacingXSmall))
        Text(
            text = stringResource(message),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun GridSquare(
    modifier: Modifier = Modifier,
    xOffset: Dp,
    yOffset: Dp
) {
    Box(
        modifier = modifier
            .offset(x = xOffset, y = yOffset)
            .size(GridSquareSize)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraSmall
            )
    )
}

@Composable
fun DataEmptyIndicator(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    @StringRes message: Int = R.string.default_no_data_message
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(SpacingSmall))
        Text(
            text = stringResource(message),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

private const val DURATION = 500
private const val DELAY = 200L
private const val INTERVAL_DURATION = 5_000

private val ListBarWidth = 24.dp
private val ListBarHeight = 4.dp

private val GridContainerSize = 24.dp
private val GridSquareSize = 8.dp