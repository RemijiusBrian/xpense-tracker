package dev.ridill.xpensetracker.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dev.ridill.xpensetracker.core.ui.theme.PaddingMedium
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun XTSnackbarHost(
    snackbarController: SnackbarController = rememberSnackbarController()
) {
    SnackbarHost(hostState = snackbarController.snackbarHostState) { data ->
        Snackbar(
            action = {
                TextButton(onClick = { data.performAction() }) {
                    Text(data.visuals.actionLabel.orEmpty())
                }
            },
            modifier = Modifier
                .padding(PaddingMedium),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(data.visuals.message)
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

    fun showSnackbar(
        message: String,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        snackbarJob?.cancel()
        snackbarJob = coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = withDismissAction,
                duration = duration
            )
        }
    }

    fun showSnackbarWithAction(
        message: String,
        actionLabel: String,
        onActionPerformed: (SnackbarResult) -> Unit,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        snackbarJob?.cancel()
        snackbarJob = coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = withDismissAction,
                duration = duration,
                actionLabel = actionLabel
            )
            onActionPerformed(result)
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