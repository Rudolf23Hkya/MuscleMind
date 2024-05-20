package com.elte_r532ov.musclemind.ui.screens.workouts.createWorkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.SelectedWorkout
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.util.Resource
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedCreateWorkoutViewModel @Inject constructor(
    private val repository: MuscleMindRepository
)
    : ViewModel() {

    // Util
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }



    private suspend fun getRecommendedWorkouts(){
        when (val result = repository.getRecomWorkouts()) {
            //Navigating to user-s Active Workouts if Success
            is Resource.Success -> {
                //If the recommended workouts arrive from the server they can be displayed
                sendUiEvent(UiEvent.Navigate(Routes.CREATE_WORKOUT_SELECT))
            }
            is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(result.message!!))
        }
    }
    /*
    private suspend fun postSelectedWorkout() {
        val selectedWorkout = Workout()
        val selectedWorkoutData = SelectedWorkout()

        when (val result = repository.postUserWorkout()) {
            //Navigating to user-s Active Workouts if Success
            is Resource.Success -> {
                //If the recommended workouts arrive from the server they can be displayed
                sendUiEvent(UiEvent.Navigate(Routes.CREATE_WORKOUT_SELECT))
            }
            is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(result.message!!))
        }
    }
    */

    }