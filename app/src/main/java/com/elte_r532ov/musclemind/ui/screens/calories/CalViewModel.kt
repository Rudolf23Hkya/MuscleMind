package com.elte_r532ov.musclemind.ui.screens.calories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.CaloriesData
import com.elte_r532ov.musclemind.data.api.Resource
import com.elte_r532ov.musclemind.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CalViewModel @Inject constructor(
    private val repository: MuscleMindRepository
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _consumedKcal = MutableStateFlow("")
    val consumedKcal: StateFlow<String> = _consumedKcal

    init {
        viewModelScope.launch {
            refreshCal()
        }
    }

    fun addCal(kcal: String) {
        viewModelScope.launch {
            refreshCal()

            try {
                val kcalInt = kcal.toInt()
                when (val result = repository.addCalories(
                    CaloriesData(kcalInt)
                )) {
                    is Resource.Success -> {
                        refreshCal()
                    }
                    is Resource.Error -> {
                        result.message?.let { UiEvent.ErrorOccured(it) }?.let { _uiEvent.send(it) }
                    }
                }
            } catch (e: NumberFormatException) {
                val errorMessage = "Invalid kcal value. Please enter a valid number."
                _uiEvent.send(UiEvent.ErrorOccured(errorMessage))
            }
        }
    }

    private suspend fun refreshCal(){
        when (val result = repository.getCalories()) {
            is Resource.Success -> {
                result.data?.let {
                    _consumedKcal.value = it.calories.toString()
                }
            }
            is Resource.Error -> {
                result.message?.let { UiEvent.ErrorOccured(it) }?.let { _uiEvent.send(it) }
            }
        }
    }

}