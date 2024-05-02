package com.elte_r532ov.musclemind.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsSharedViewModel@Inject constructor(
    private val accountRepository: MuscleMindRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private lateinit var _userInfo : UserData


    init {
        viewModelScope.launch {
            try {
                _userInfo = accountRepository.getUserData().data!!
            }
            catch (e: NumberFormatException){
                sendUiEvent(UiEvent.ErrorOccured("User Data Error!"))
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
                            email=email,
                            username = name,
                            gender = _userInfo.gender,
                            experiencelevel = _userInfo.experiencelevel,
                            age = intAge,
                            weight = dWeight,
                            height = dHeight)
                        if(!accountRepository.modifyUserData(userToModify))
                            _uiEvent.send(UiEvent.ErrorOccured("Invalid Password!"))
                        _uiEvent.send(UiEvent.Navigate(Routes.SETTINGS_MAIN))
                    }
                    catch (e: NumberFormatException){
                        _uiEvent.send(UiEvent.ErrorOccured("Network error!"))
                    }
                }

    }
    public fun deleteAccount(delStr : String,password : String){
        if(delStr == "DELETE"){
            if(true){
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