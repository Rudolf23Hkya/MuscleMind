package com.elte_r532ov.musclemind.ui.screens.login

sealed class LoginEvent {

    object onSignUpClicked : LoginEvent()

    data class onLoginClicked(val eMail: String, val password: String) : LoginEvent()

    object onContinueWithGoogle : LoginEvent()
}