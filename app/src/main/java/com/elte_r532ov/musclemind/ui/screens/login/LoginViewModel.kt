package com.elte_r532ov.musclemind.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.userData.MuscleMindRepository
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.data.userData.UserData
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent
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

    private var rememberMeClicked = false

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.onLoginClicked -> {
                viewModelScope.launch {
                    //Login logic
                    if (repository.loginAttempt(event.eMail, event.password)) {
                        //Changing the view
                        sendUiEvent(UiEvent.Navigate(Routes.WORKOUTS_ACTIVE))
                    } else {
                        sendUiEvent(UiEvent.ErrorOccured("Invalid E-mail/username or password!"))
                    }
                }
            }
            is LoginEvent.onContinueWithGoogle -> {
                sendUiEvent(UiEvent.Navigate("registration"))
            }
            is LoginEvent.onSignUpClicked -> {
                sendUiEvent(UiEvent.Navigate("registration"))
            }
            is LoginEvent.onForgotPassword -> {

            }
            is LoginEvent.onRememberMeChange -> {
                //Changing the state of the remember me clicked box
                rememberMeClicked = !rememberMeClicked
            }
        }
    }


    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}