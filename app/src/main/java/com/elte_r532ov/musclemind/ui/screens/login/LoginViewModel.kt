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
    private val repository: MuscleMindRepository,
    private val sessionManagement: SessionManagement
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    private var userData : UserData? = null

    private var rememberMeClicked = false

   /* var eMail by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    */

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.onLoginClicked -> {
                viewModelScope.launch {
                    Log.d(event.eMail, "E-Mail")
                    Log.d(event.password, "Password")

                    userData = repository.loginAttempt(event.eMail, event.password)

                    if (userData != null) {
                        //Creating session TODO Login needs to be done trough the API
                        sessionManagement.saveSessionToken("")
                        //Changing the view
                        sendUiEvent(UiEvent.Navigate(Routes.WORKOUTS_ACTIVE))
                    } else {
                        val msg = "No such e-mail in our Database"
                        Log.d(msg, event.eMail)
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