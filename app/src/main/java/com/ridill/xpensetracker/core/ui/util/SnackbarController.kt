package com.ridill.xpensetracker.core.ui.util

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackbarController(
    private val coroutineScope: CoroutineScope
) {
    private var snackbarJob: Job? = null

    init {
        cancelActiveJob()
    }

    fun showSnackbar(
        scaffoldState: ScaffoldState,
        message: String,
        actionLabel: String? = null,
        onActionPerformed: ((SnackbarResult) -> Unit)? = null
    ) {
        if (snackbarJob != null) cancelActiveJob()
        snackbarJob = coroutineScope.launch {
            val result =
                scaffoldState.snackbarHostState.showSnackbar(message, actionLabel = actionLabel)
            if (actionLabel != null && onActionPerformed != null) {
                onActionPerformed(result)
            }
            cancelActiveJob()
        }
    }

    fun showSnackbar(
        scaffoldState: BottomSheetScaffoldState,
        message: String,
        actionLabel: String? = null,
        onActionPerformed: ((SnackbarResult) -> Unit)? = null
    ) {
        if (snackbarJob != null) cancelActiveJob()
        snackbarJob = coroutineScope.launch {
            val result =
                scaffoldState.snackbarHostState.showSnackbar(message, actionLabel = actionLabel)
            if (actionLabel != null && onActionPerformed != null) {
                onActionPerformed(result)
            }
            cancelActiveJob()
        }
    }

    private fun cancelActiveJob() {
        snackbarJob?.cancel()
        snackbarJob = Job()
    }
}

@Composable
fun rememberSnackbarController(coroutineScope: CoroutineScope): SnackbarController =
    remember(coroutineScope) { SnackbarController(coroutineScope) }