package com.ridill.xpensetracker.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.CornerRadiusSmall
import com.ridill.xpensetracker.core.ui.theme.PaddingExtraSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingExtraSmall
import kotlinx.coroutines.delay

@Composable
fun EmptyListIndicator(
    modifier: Modifier = Modifier,
    @StringRes message: Int? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
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
        animationValues.forEach { animationValue ->
            Box(
                modifier = Modifier
                    .offset(y = animationValue.dp)
                    .width(24.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colors.primary)
            )
            Spacer(modifier = Modifier.height(SpacingExtraSmall))
        }
        Spacer(modifier = Modifier.height(SpacingExtraSmall))
        Text(
            text = stringResource(message ?: R.string.list_empty),
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun EmptyGridIndicator(
    modifier: Modifier = Modifier,
    @StringRes message: Int? = null
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
                .padding(PaddingExtraSmall)
                .size(24.dp)
        ) {
            Square(
                modifier = Modifier
                    .align(Alignment.TopStart),
                xOffset = -animationValues[0].dp,
                yOffset = -animationValues[0].dp
            )
            Square(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                xOffset = animationValues[1].dp,
                yOffset = -animationValues[1].dp
            )
            Square(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                xOffset = -animationValues[2].dp,
                yOffset = animationValues[2].dp
            )
            Square(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                xOffset = animationValues[3].dp,
                yOffset = animationValues[3].dp
            )
        }
        Spacer(modifier = Modifier.height(SpacingExtraSmall))
        Text(
            text = stringResource(message ?: R.string.grid_empty),
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
private fun Square(
    modifier: Modifier = Modifier,
    xOffset: Dp,
    yOffset: Dp
) {
    Box(
        modifier = modifier
            .offset(x = xOffset, y = yOffset)
            .size(SquareSize)
            .clip(RoundedCornerShape(CornerRadiusSmall))
            .background(MaterialTheme.colors.primary)
    )
}

private const val DURATION = 500
private const val DELAY = 100L
private const val INTERVAL_DURATION = 3_000
private val SquareSize = 8.dp