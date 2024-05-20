package com.elte_r532ov.musclemind.ui.screens.workouts.createWorkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.WorkoutApi
import com.elte_r532ov.musclemind.ui.util.OptiListElement
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

    private var trx : Boolean = false
    private var weightlifting : Boolean = false

    private var doWeekly by mutableIntStateOf(0)

    private val _recommendedWorkouts = MutableStateFlow<List<WorkoutApi>>(emptyList())
    val recommendedWorkouts: StateFlow<List<WorkoutApi>> = _recommendedWorkouts


    init{
        viewModelScope.launch {
            when (val result = repository.updateAccessToken()) {
                is Resource.Success -> {
                }
                is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(result.message!!))
            }
        }
    }
    fun workoutDataSet(equipmentList: List<OptiListElement>, doWeekly: String){
        try {
            val doWeeklyTemp = doWeekly.toInt()

            if(doWeeklyTemp in 1..7){
                this.trx = equipmentList.any { it.name == "Trx" && it.isSelected }
                this.weightlifting = equipmentList.any { it.name == "Dumbbells" && it.isSelected }

                this.doWeekly = doWeeklyTemp
                // Getting the next screen s data
                viewModelScope.launch {
                    getRecommendedWorkouts()
                }
            }
            else{
                sendUiEvent(UiEvent.ErrorOccured("The workout value needs to be between 1-7!"))
            }
        } catch (e: NumberFormatException) {
            sendUiEvent(UiEvent.ErrorOccured("The workout value needs to be a natural number!"))
        }
    }



    private suspend fun getRecommendedWorkouts() {
        when (val result = repository.getRecomWorkouts(
            trx = this.trx, weightlifting = this.weightlifting
        )) {
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