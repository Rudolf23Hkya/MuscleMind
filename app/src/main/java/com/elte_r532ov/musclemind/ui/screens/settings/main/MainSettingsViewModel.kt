package com.elte_r532ov.musclemind.ui.screens.settings.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainSettingsViewModel@Inject constructor(
    private val sessionManagement: SessionManagement
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event : MainSettingsEvent){
        when(event){
            MainSettingsEvent.onChangePasswordClicked ->
                sendUiEvent(UiEvent.Navigate(Routes.SETTINGS_PASSWORD))
            MainSettingsEvent.onChangeAccountDataClicked ->
                sendUiEvent(UiEvent.Navigate(Routes.SETTINGS_CHANGE_ACCOUNT_DATA))
            MainSettingsEvent.onLogOutClicked -> {
                //Forgetting the session
                sessionManagement.delSessionToken()
                sendUiEvent(UiEvent.Navigate(Routes.LOGIN))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}