package com.elte_r532ov.musclemind.ui.screens.login

import androidx.compose.runtime.MutableState

sealed class LoginEvent {

    object onSignUpClicked : LoginEvent()
    object onForgotPassword : LoginEvent()

    object onRememberMeChange: LoginEvent()
    data class onLoginClicked(val eMail: String, val password: String) : LoginEvent()

    data class onContinueWithGoogle(val  eMail: String,val password : String) : LoginEvent()
}