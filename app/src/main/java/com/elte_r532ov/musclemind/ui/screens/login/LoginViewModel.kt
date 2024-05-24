package com.elte_r532ov.musclemind.ui.screens.login

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

    private var rememberMeClicked = false

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.onLoginClicked -> {
                viewModelScope.launch {
                    //Login logic
                    val result = repository.loginAttempt(event.eMail, event.password)

                    when (result) {
                        is Resource.Success -> sendUiEvent(UiEvent.Navigate(Routes.WORKOUT_ACTIVE))
                        is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(result.message!!))
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