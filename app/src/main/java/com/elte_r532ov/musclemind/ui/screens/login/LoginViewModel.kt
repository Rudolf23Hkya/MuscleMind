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
        viewModelScope.launch {
            // Sending the token to the server for validation
            when (val result = repository.googleTokenAuth(idToken)) {
                is Resource.Success -> {
                    val fullAutUserData = result.data
                    if (fullAutUserData != null) {
                        if (fullAutUserData.userData.username != "EMAIL_NOT_FOUND") {
                            // If the Google account's email matches a registered email, the login is successful
                            sendUiEvent(UiEvent.Navigate(Routes.WORKOUT_ACTIVE_ROUTE))
                        } else {
                            // If the username is "USER NOT FOUND", the user has no account
                            // with the Google account s e-mail address.
                            // The user needs to create an account with the e-mail address.
                            sendUiEvent(UiEvent.ShowSnackbar(
                                    "No account found with this Google account e-mail address." +
                                    "Please create an account first!"))
                        }
                    }
                }
                is Resource.Error -> {
                    sendUiEvent(UiEvent.ShowSnackbar(result.message ?: "Unknown error occurred"))
                }
            }
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