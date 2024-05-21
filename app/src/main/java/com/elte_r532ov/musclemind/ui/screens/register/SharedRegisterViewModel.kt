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
import com.elte_r532ov.musclemind.data.api.responses.Disease
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.ui.util.OptiListElement
import com.elte_r532ov.musclemind.data.api.Resource
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.UiEvent
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

    private var asthma : Boolean = false
    private var bad_knee : Boolean = false
    private var cardiovascular_d : Boolean = false
    private var osteoporosis : Boolean = false

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.onGenderChosen -> handleGenderChosen(event.gender)
            is RegisterEvent.onFizDataChosen -> handleFizDataChosen(event.weight, event.age, event.height)
            is RegisterEvent.onExperienceChosen -> handleExperienceChosen(event.exp)
            is RegisterEvent.onUserDataChosen -> handleUserDataChosen(event.fstPassword, event.sndPassword, event.email, event.name)
            is RegisterEvent.onDiseasesChosen -> handleDiseasesChosen(event.diseases)
        }
    }

    private fun handleGenderChosen(gender: Gender?) {
        if (gender == null) {
            sendUiEvent(UiEvent.ErrorOccured("You need to select your gender!"))
        } else {
            this.gender = gender
            Log.d("MyActivity", this.gender.toString())
            sendUiEvent(UiEvent.Navigate(Routes.REGISTER_FIZ_DATA))
        }
    }

    //Fiz data
    private fun handleFizDataChosen(weight: String, age: String, height: String) {
        try {
            val eWeight = weight.toDouble()
            val eAge = age.toInt()
            val eHeight = height.toDouble()

            when {
                !isValidWeight(eWeight) -> sendUiEvent(UiEvent.ErrorOccured("Invalid weight! We only can process users between 20-500 kg."))
                !isValidAge(eAge) -> sendUiEvent(UiEvent.ErrorOccured("You must be between 12 and 120 years."))
                !isValidHeight(eHeight) -> sendUiEvent(UiEvent.ErrorOccured("Invalid height! Height must be between 50 and 251 cm."))
                else -> {
                    this.weight = eWeight
                    this.age = eAge
                    this.height = eHeight
                    sendUiEvent(UiEvent.Navigate(Routes.REGISTER_EXP))
                }
            }
        } catch (e: NumberFormatException) {
            sendUiEvent(UiEvent.ErrorOccured("Invalid number format!"))
        }
    }

    private fun isValidWeight(weight: Double): Boolean {
        return weight in 20.0..500.0
    }

    private fun isValidAge(age: Int): Boolean {
        return age in 12..120
    }

    private fun isValidHeight(height: Double): Boolean {
        return height in 50.0..251.0
    }

    // Experience
    private fun handleExperienceChosen(exp: ExperienceLevel?) {
        if (exp == null) {
            sendUiEvent(UiEvent.ErrorOccured("You need to select your experience level!"))
        } else {
            this.experienceLevel = exp
            sendUiEvent(UiEvent.Navigate(Routes.REGISTER_DISEASE))
        }
    }
    // Disease
    private fun handleDiseasesChosen(diseaseList: List<OptiListElement>) {
            this.asthma = diseaseList.any { it.name == "Asthma" && it.isSelected }
            this.bad_knee = diseaseList.any { it.name == "Bad Knee" && it.isSelected }
            this.cardiovascular_d = diseaseList.any { it.name == "Cardiovascular Disease" && it.isSelected }
            this.osteoporosis = diseaseList.any { it.name == "Osteoporosis" && it.isSelected }
        // No condition because it s optional
        sendUiEvent(UiEvent.Navigate(Routes.REGISTER_ACCOUNT_DATA))
    }

    // User data
    private fun handleUserDataChosen(fstPassword: String, sndPassword: String, email: String, name: String) {
        when {
            fstPassword != sndPassword -> sendUiEvent(UiEvent.ErrorOccured("Not Matching Passwords!"))
            fstPassword.length < 6 -> sendUiEvent(UiEvent.ErrorOccured("Password is too short!"))
            !email.isValidEmail() -> sendUiEvent(UiEvent.ErrorOccured("Invalid E-mail address!"))
            name.length < 2 -> sendUiEvent(UiEvent.ErrorOccured("Username must be at least 2 characters long!"))
            !isValidUsername(name) -> sendUiEvent(
                UiEvent.ErrorOccured
                ("Enter a valid username. " +
                    "This value may contain only letters, numbers, and @/./+/-/_ characters."))
            else -> saveUserData(fstPassword, email, name)
        }
    }
    private fun CharSequence?.isValidEmail() = !isNullOrEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun isValidUsername(username: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9@./+\\-_]+$")
        return regex.matches(username)
    }

    // Saving user data
    private fun saveUserData(password: String, email: String, name: String) {
        this.password = password
        this.name = name
        this.email = email

        viewModelScope.launch {
            try {
                sendRegRequest()
            } catch (e: Exception) {
                Log.e("MyActivity", e.toString())
            }
        }
    }
    private suspend fun sendRegRequest() {
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
        val disease = Disease(
            asthma=this.asthma,
            bad_knee = this.bad_knee,
            cardiovascular_d = this.cardiovascular_d,
            osteoporosis = this.osteoporosis)

        when (val result = repository.registerUser(newUser,disease)) {
            //Navigating to user-s Active Workouts if Success
            is Resource.Success -> sendUiEvent(UiEvent.Navigate(Routes.WORKOUTS_ACTIVE))
            is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(result.message!!))
        }
    }
    // Util
    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
