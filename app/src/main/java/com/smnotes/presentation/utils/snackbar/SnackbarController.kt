package com.smnotes.presentation.utils.snackbar

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * If a snackbar is visible and the user triggers a second snackbar to show, it will remove
 * the first one and show the second. Likewise with a third, fourth, ect...
 *
 * If a mechanism like this is not used, snackbar get added to the Scaffolds "queue", and will
 * show one after another. I don't like that.
 *
 */
@ExperimentalMaterialApi
class SnackbarController
constructor(
    private val scope: CoroutineScope
) {

    private var snackbarJob: Job? = null

    init {
        cancelActiveJob()
    }

    fun getScope() = scope

    fun showSnackbar(
        snackbarHostState: SnackbarHostState,
        message: String,
        actionLabel: String? = null,
        callBackResult: (SnackbarResult) -> Unit = {},
    ) {
        if (snackbarJob == null) {
            snackbarJob = scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                )
                cancelActiveJob()
                callBackResult(result)
            }
        } else {
            cancelActiveJob()
            snackbarJob = scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel
                )
                cancelActiveJob()
                callBackResult(result)
            }
        }

    }

    private fun cancelActiveJob() {
        snackbarJob?.let { job ->
            job.cancel()
            snackbarJob = Job()
        }
    }
}