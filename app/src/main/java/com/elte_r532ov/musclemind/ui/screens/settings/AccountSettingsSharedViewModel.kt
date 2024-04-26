package com.elte_r532ov.musclemind.ui.screens.settings

import androidx.compose.material.icons.Icons
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.data.userData.ExperienceLevel
import com.elte_r532ov.musclemind.data.userData.Gender
import com.elte_r532ov.musclemind.data.userData.MuscleMindRepository
import com.elte_r532ov.musclemind.data.userData.UserData
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsSharedViewModel@Inject constructor(
    private val accountRepository: MuscleMindRepository,
    private val sessionManagement: SessionManagement
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private lateinit var _userInfo : UserData

    private var seshToken = ""


    init {
        viewModelScope.launch {
            try {
                seshToken = sessionManagement.getSessionToken()!!
                _userInfo = accountRepository.getUserBySessionToken(seshToken)!!

            }
            catch (e: NumberFormatException){
                sendUiEvent(UiEvent.ErrorOccured("Network error!"))
            }
        }
    }
    public fun changeProfileData(email: String,name : String,
        age : String,weight: String,height : String){
                //User authentication successful, data can be changed
                viewModelScope.launch {
                    try {
                        val intAge = age.toInt()
                        val dWeight = weight.toDouble()
                        val dHeight = height.toDouble()

                        val userToModify = UserData(
                            _userInfo.id,email,name,_userInfo.password,
                            _userInfo.gender,_userInfo.experienceLevel,
                            intAge,dWeight,dHeight)
                        if(!accountRepository.modifyUserData(userToModify))
                            _uiEvent.send(UiEvent.ErrorOccured("Invalid Password!"))
                        _uiEvent.send(UiEvent.Navigate(Routes.SETTINGS_MAIN))
                    }
                    catch (e: NumberFormatException){
                        _uiEvent.send(UiEvent.ErrorOccured("Network error!"))
                    }
                }

    }
    public fun changePassword(newPassword : String, newPasswordAgain : String, oldPassword : String){
        if(newPassword == newPasswordAgain){
            sendUiEvent(UiEvent.Navigate(Routes.SETTINGS_MAIN))
            viewModelScope.launch {
                val oldUser = _userInfo
                if(oldUser.password == oldPassword){
                    val userToMod = UserData(
                        oldUser.id,oldUser.email,oldUser.name, newPassword,oldUser.gender,oldUser.experienceLevel,
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
            if(_userInfo.password == password){
                viewModelScope.launch {
                    accountRepository.deleteUserData(_userInfo)
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