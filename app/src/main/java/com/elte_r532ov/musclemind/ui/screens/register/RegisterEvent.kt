package com.elte_r532ov.musclemind.ui.screens.register
import  com.elte_r532ov.musclemind.data.userData.Gender
import com.elte_r532ov.musclemind.data.userData.ExperienceLevel

//I use everywhere strings, cuz the view model does the conversion and the error handling
sealed class RegisterEvent {
    data class onGenderChosen(val  gender: Gender?) : RegisterEvent()
    data class onFizDataChosen(val weight: String, val age: String,val height: String ) : RegisterEvent()
    data class onExperienceChosen(val  exp: ExperienceLevel?) : RegisterEvent()
    data class onUserDataChosen(val  name: String,val email: String,
                                val fstPassword: String, val sndPassword: String) : RegisterEvent()

    object onGenderBackNav : RegisterEvent()

    object onFizDataBackNav : RegisterEvent()
    object onExperienceBackNav : RegisterEvent()
    object onUserDataBackNav : RegisterEvent()

}