package com.elte_r532ov.musclemind.ui.screens.register

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.enums.ExperienceLevel
import com.elte_r532ov.musclemind.data.enums.Gender
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.util.Resource
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedRegisterViewModel @Inject constructor(
    private val repository: MuscleMindRepository
)
    : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private var name by mutableStateOf("")
    private var email by mutableStateOf("")
    private var password by mutableStateOf("")
    private var gender by mutableStateOf<Gender?>(null)
    private var experienceLevel by mutableStateOf<ExperienceLevel?>(null)
    private var age by mutableIntStateOf(0)
    private var weight by mutableDoubleStateOf(0.0)
    private var height by mutableDoubleStateOf(0.0)


    fun onEvent(event : RegisterEvent){
        when(event){
            //Setters and Navs
            is RegisterEvent.onGenderChosen -> {
                if(event.gender == null){
                    sendUiEvent(UiEvent.ErrorOccured("You need to choose!"))
                }
                if(event.gender == Gender.MALE){
                    this.gender = Gender.MALE
                }
                else{
                    this.gender = Gender.FEMALE
                }

                Log.d("MyActivity", this.gender.toString())
                sendUiEvent(UiEvent.Navigate(Routes.REGISTER_FIZ_DATA))
            }

            is RegisterEvent.onFizDataChosen -> {
                try {
                    val eWeight = event.weight.toDouble()
                    val eAge = event.age.toInt()
                    val eHeight = event.height.toDouble()

                    //The tallest man alive is 251 - Sultan Kosen
                    if(eWeight > 20 && eWeight < 1500 &&
                        eAge > 12  && eAge < 120 &&
                        eHeight > 100 && eHeight < 251) {
                        this.weight = eWeight
                        this.age = eAge
                        this.height = eHeight

                        sendUiEvent(UiEvent.Navigate(Routes.REGISTER_EXP))
                    }
                    else{
                        sendUiEvent(UiEvent.ErrorOccured("Invalid values!"))
                    }
                }
                catch (e: NumberFormatException){
                    sendUiEvent(UiEvent.ErrorOccured("Invalid number format!"))
                }
            }
            is RegisterEvent.onExperienceChosen -> {
                if(event.exp == null){
                    sendUiEvent(UiEvent.ErrorOccured("You need to choose!"))
                }
                else{
                    this.experienceLevel = event.exp

                    sendUiEvent(UiEvent.Navigate(Routes.REGISTER_ACCOUNT_DATA))
                }
            }
            is RegisterEvent.onUserDataChosen -> {
                //Data validation
                if((event.fstPassword != event.sndPassword)){
                    sendUiEvent(UiEvent.ErrorOccured("Not Matching Passwords!"))
                }
                else if(event.fstPassword.length < 6){
                    sendUiEvent(UiEvent.ErrorOccured("Password is too short!"))
                }
                else if(!event.email.isValidEmail()){
                    sendUiEvent(UiEvent.ErrorOccured("Invalid E-mail address!"))
                }
                else if(event.name.length < 2){
                    sendUiEvent(UiEvent.
                    ErrorOccured("Username must be at least 2 characters long!"))
                }
                else{
                    //Validated data can be saved to DB
                    this.password = event.fstPassword
                    this.name = event.name
                    this.email = event.email

                    viewModelScope.launch{
                        try{
                            //Adding the new User To the DB
                           addUserToDB()
                        }
                        catch (e: Exception){
                            Log.e("MyActivity",e.toString() )
                        }
                    }
                }
            }
        }
    }
    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
    private suspend fun addUserToDB() {
        //Adding new user to the database
        val newUser = UserData(
            email = this.email,
            username = this.name,
            password = this.password,
            gender = this.gender!!,
            experiencelevel = this.experienceLevel!!,
            age = this.age,
            weight = this.weight,
            height = this.height
        )

        val result = repository.insertUserData(newUser)

        when (result) {
            //Navigating to user-s Active Workouts if Success
            is Resource.Success -> sendUiEvent(UiEvent.Navigate(Routes.WORKOUTS_ACTIVE))
            is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(result.message!!))
        }
    }
    private fun CharSequence?.isValidEmail() = !isNullOrEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
