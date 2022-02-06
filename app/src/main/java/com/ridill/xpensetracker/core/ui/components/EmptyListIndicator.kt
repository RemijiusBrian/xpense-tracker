package com.ridill.xpensetracker.core.ui.components

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
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.SpacingExtraSmall
import kotlinx.coroutines.delay

@Composable
fun EmptyListIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val animationValues = (1..3).map { index ->
            var offset by remember { mutableStateOf(0f) }

            LaunchedEffect(Unit) {
                delay(Delay * index)
                animate(
                    initialValue = 0f,
                    targetValue = -12f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = Duration,
                            delayMillis = IntervalDuration
                        ),
                        repeatMode = RepeatMode.Reverse,
                    )
                ) { animatedValue, _ -> offset = animatedValue }
            }
            offset
        }

        (animationValues).forEach { animationValue ->
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
            text = stringResource(R.string.list_empty),
            style = MaterialTheme.typography.caption
        )
    }
}

private const val Duration = 500
private const val Delay = 100L
private const val IntervalDuration = 5_000