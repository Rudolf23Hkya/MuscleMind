package com.elte_r532ov.musclemind.ui.screens.settings

import com.elte_r532ov.musclemind.data.userData.ExperienceLevel
import com.elte_r532ov.musclemind.data.userData.Gender
import com.elte_r532ov.musclemind.ui.screens.register.RegisterEvent

sealed class MainSettingsEvent {

    object onLogOutClicked : MainSettingsEvent()

    object onChangeProfileDataClicked : MainSettingsEvent()
    object onAccountSettingsClicked : MainSettingsEvent()
}