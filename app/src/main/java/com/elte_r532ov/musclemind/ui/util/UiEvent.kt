package com.elte_r532ov.musclemind.ui.util

//This event is for UI in general
sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(
        val message: String
    ) : UiEvent()
    data class ErrorOccured(val errMsg: String) : UiEvent()

    data class ShowFloatingActionButton(val show: Boolean) : UiEvent()

}