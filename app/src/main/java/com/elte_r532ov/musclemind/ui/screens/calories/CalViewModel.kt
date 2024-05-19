package com.elte_r532ov.musclemind.ui.screens.calories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.CaloriesData
import com.elte_r532ov.musclemind.util.Resource
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CalViewModel @Inject constructor(
    private val repository: MuscleMindRepository
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun addCal(kcal: String){
        viewModelScope.launch {
            // Call the repository function and store the result
            // Check the result type using a when statement
            when (val result = repository.updateAccessToken()) {
                is Resource.Success -> {
                    _uiEvent.send(UiEvent.ErrorOccured("Működött a token"))
                }
                is Resource.Error -> {
                    result.message?.let { UiEvent.ErrorOccured(it) }?.let { _uiEvent.send(it) }
                }
            }
        }
    }


    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}