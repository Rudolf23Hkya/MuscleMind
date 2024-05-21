package com.elte_r532ov.musclemind.ui.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow

@Composable
fun handleUiEvent(
    uiEvent: Flow<UiEvent>,
    onNavigate: NavHostController
): SnackbarHostState {
    var snackBarMessage by remember { mutableStateOf<String?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate.navigate(event.route)
                is UiEvent.ShowSnackbar -> snackBarMessage = event.message
                is UiEvent.ErrorOccured -> snackBarMessage = event.errMsg
                else -> Unit
            }
        }
    }

    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let { message ->
            snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
            snackBarMessage = null
        }
    }

    return snackBarHostState
}