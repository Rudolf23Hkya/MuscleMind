package com.elte_r532ov.musclemind.ui.screens.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: MuscleMindRepository) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var rememberMeClicked = false;

   /* var eMail by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    */

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.onLoginClicked -> {
                Log.d(event.eMail, "E-Mail")
                Log.d(event.password, "Password")
            }
            is LoginEvent.onContinueWithGoogle -> {
                sendUiEvent(UiEvent.Navigate(Routes.REGISTER_DATA_PAGE))
            }
            is LoginEvent.onSignUpClicked -> {
                sendUiEvent(UiEvent.Navigate(Routes.REGISTER_DATA_PAGE))
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