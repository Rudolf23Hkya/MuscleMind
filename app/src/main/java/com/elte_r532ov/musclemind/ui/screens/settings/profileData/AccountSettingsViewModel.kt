package com.elte_r532ov.musclemind.ui.screens.settings.profileData

import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.data.userData.ExperienceLevel
import com.elte_r532ov.musclemind.data.userData.Gender
import com.elte_r532ov.musclemind.data.userData.MuscleMindRepository
import com.elte_r532ov.musclemind.data.userData.UserData
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel@Inject constructor(
    private val accountRepository: MuscleMindRepository,
    private val sessionManagement: SessionManagement
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _userInfo = MutableLiveData<UserData>()
    val userInfo: LiveData<UserData> = _userInfo

    private var seshToken = ""

    init {
        viewModelScope.launch {
            try {
                seshToken = sessionManagement.getSessionToken()!!
                _userInfo.value = accountRepository.getUserBySessionToken(seshToken!!)
            }
            catch (e: NumberFormatException){
                sendUiEvent(UiEvent.ErrorOccured("Network error!"))
            }

        }
    }
    public fun changeProfileData(email: String,name : String,gender :Gender,exp : ExperienceLevel,
        age : String,weight: String,height : String,password : String){
            if(_userInfo.value != null && _userInfo.value!!.password == password){
                //User authentication successful, data can be changed
                viewModelScope.launch {
                    try {
                        val intAge = age.toInt()
                        val dWeight = weight.toDouble()
                        val dHeight = height.toDouble()

                        val userToModify = UserData(
                            _userInfo.value!!.id,
                            seshToken,email,name,password,
                            gender,exp,intAge,dWeight,dHeight)
                        if(!accountRepository.modifyUserData(userToModify))
                            sendUiEvent(UiEvent.ErrorOccured("Invalid Password!"))

                    }
                    catch (e: NumberFormatException){
                        sendUiEvent(UiEvent.ErrorOccured("Network error!"))
                    }
                }
            }
        else{
                sendUiEvent(UiEvent.ErrorOccured("Invalid Password or Login token."))
        }

    }
    public fun changePassword(newPassword : String, newPasswordAgain : String, oldPassword : String){
        if(newPassword == newPasswordAgain){
            viewModelScope.launch {
                val oldUser = _userInfo.value!!
                if(oldUser.password == oldPassword){
                    val userToMod = UserData(
                        oldUser.id,
                        seshToken,oldUser.email,oldUser.name, newPassword,oldUser.gender,oldUser.experienceLevel,
                        oldUser.age,oldUser.weight,oldUser.height)

                    accountRepository.modifyPassword(userToMod)
                }
                else
                    sendUiEvent(UiEvent.ErrorOccured("Incorrect Account Password!"))
            }
        }
        else
            sendUiEvent(UiEvent.ErrorOccured("Passwords do not match!"))
    }
    public fun deleteAccount(delStr : String,password : String){
        if(delStr == "DELETE"){
            if(_userInfo.value!!.password == password){
                viewModelScope.launch {
                    accountRepository.deleteUserData(_userInfo.value!!)
                }
            }
            else{
                sendUiEvent(UiEvent.ErrorOccured("Incorrect Account Password!"))
            }
        }
        else
            sendUiEvent(UiEvent.ErrorOccured("Please enter DELETE to delete your account!"))
    }


    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}