package dev.ridill.xpensetracker.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.theme.SpacingMedium
import dev.ridill.xpensetracker.core.ui.theme.SpacingSmall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun XTSnackbarHost(
    snackbarController: SnackbarController = rememberSnackbarController()
) {
    SnackbarHost(hostState = snackbarController.snackbarHostState) { data ->
        XTSnackbar(
            visuals = data.visuals as XTSnackbarVisuals,
            performAction = data::performAction
        )
    }
}

@Composable
private fun XTSnackbar(
    visuals: XTSnackbarVisuals,
    performAction: () -> Unit
) {
    val containerColor = when (visuals) {
        is XTSnackbarVisuals.Error -> MaterialTheme.colorScheme.errorContainer
        is XTSnackbarVisuals.Message -> MaterialTheme.colorScheme.inverseSurface
    }
    Snackbar(
        action = if (!visuals.actionLabel.isNullOrEmpty()) {
            {
                TextButton(onClick = performAction) {
                    Text(visuals.actionLabel.orEmpty())
                }
            }
        } else null,
        shape = MaterialTheme.shapes.medium,
        containerColor = containerColor,
        contentColor = contentColorFor(backgroundColor = containerColor),
        modifier = Modifier
            .padding(SpacingMedium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (visuals is XTSnackbarVisuals.Error) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = stringResource(R.string.content_description_error),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(SpacingSmall))
            }
            Text(visuals.message)
        }
    }
}

class SnackbarController(
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    private var snackbarJob: Job? = null

    init {
        snackbarJob?.cancel()
    }

    fun showMessageSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onActionPerformed: ((SnackbarResult) -> Unit)? = null
    ) {
        snackbarJob?.cancel()
        snackbarJob = coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                visuals = XTSnackbarVisuals.Message(
                    actionLabel = actionLabel,
                    duration = duration,
                    message = message,
                    withDismissAction = withDismissAction
                )
            )
            if (actionLabel != null && onActionPerformed != null) {
                onActionPerformed.invoke(result)
            }
        }
    }

    fun showErrorSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onActionPerformed: ((SnackbarResult) -> Unit)? = null
    ) {
        snackbarJob?.cancel()
        snackbarJob = coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                visuals = XTSnackbarVisuals.Error(
                    actionLabel = actionLabel,
                    duration = duration,
                    message = message,
                    withDismissAction = withDismissAction
                )
            )
            if (actionLabel != null && onActionPerformed != null) {
                onActionPerformed.invoke(result)
            }
        }
    }
}

@Composable
fun rememberSnackbarController(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): SnackbarController = remember(coroutineScope, snackbarHostState) {
    SnackbarController(
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope
    )
}

// Types Of Snackbar visuals for messages and errors
sealed class XTSnackbarVisuals : SnackbarVisuals {
    data class Message(
        override val message: String,
        override val actionLabel: String? = null,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
        override val withDismissAction: Boolean = false
    ) : XTSnackbarVisuals()

    data class Error(
        override val message: String,
        override val actionLabel: String? = null,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
        override val withDismissAction: Boolean = false
    ) : XTSnackbarVisuals()
}