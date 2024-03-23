package com.elte_r532ov.musclemind.util

//This event is for UI in general
sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(
        val message: String
    ) : UiEvent()
}