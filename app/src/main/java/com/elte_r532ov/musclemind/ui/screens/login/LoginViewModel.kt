package com.elte_r532ov.musclemind.ui.screens.login

import android.credentials.CredentialManager
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.Resource
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MuscleMindRepository
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.onLoginClicked -> {
                viewModelScope.launch {
                    //Login logic
                    when (val result = repository.loginAttempt(event.eMail, event.password)) {
                        is Resource.Success -> sendUiEvent(UiEvent.Navigate(Routes.WORKOUT_ACTIVE))
                        is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(result.message!!))
                    }
                }
            }
            is LoginEvent.onContinueWithGoogle -> {

            }
            is LoginEvent.onSignUpClicked -> {
                sendUiEvent(UiEvent.Navigate(Routes.REGISTRATION_ROUTE))
            }
        }
    }

    fun sendTokenToServer(idToken: String) {
        sendUiEvent(UiEvent.ShowSnackbar(idToken))
        viewModelScope.launch {
            // Küldje el a token-t a szervernek
            // Példa HTTP kéréssel (Retrofit vagy más HTTP kliens használatával)
        }
    }

    fun onError(exception: Exception) {
        sendUiEvent(UiEvent.ErrorOccured(exception.toString()))
    }


    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}