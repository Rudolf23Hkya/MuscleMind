package com.elte_r532ov.musclemind.ui.screens.settings.main

sealed class MainSettingsEvent {

    object onLogOutClicked : MainSettingsEvent()

    object onChangeProfileDataClicked : MainSettingsEvent()
    object onAccountSettingsClicked : MainSettingsEvent()
}