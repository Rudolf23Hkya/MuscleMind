package com.elte_r532ov.musclemind.ui.screens.workouts.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.ui.util.OptiListElement
import com.elte_r532ov.musclemind.data.api.Resource
import com.elte_r532ov.musclemind.data.api.responses.Exercise
import com.elte_r532ov.musclemind.data.api.responses.SelectedWorkout
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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

    private val _recommendedWorkouts = MutableLiveData<List<Workout>>()
    val recommendedWorkouts: LiveData<List<Workout>> = _recommendedWorkouts

    // Selected workout
    private val _selectedWorkout = MutableLiveData<Workout?>()
    val selectedWorkout: LiveData<Workout?> = _selectedWorkout

    // Selected exercises
    private val _selectedExercises = MutableLiveData<List<Exercise>>()
    val selectedExercises: LiveData<List<Exercise>> = _selectedExercises
    init{
        viewModelScope.launch {
            when (val result = repository.updateAccessToken()) {
                is Resource.Success -> {
                    _recommendedWorkouts.value = emptyList()
                }
                is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(result.message!!))
            }
        }
    }

    // Callback function for changing the selected workout
    fun onWorkoutClicked(workout: Workout) {
        _selectedWorkout.value = workout
        _selectedExercises.value = workout.exercises
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
        try {
            val result = repository.getRecomWorkouts(
                trx = this.trx, weightlifting = this.weightlifting
            )
            when (result) {
                is Resource.Success -> {
                    val workouts = result.data ?: emptyList()
                    _recommendedWorkouts.value = workouts

                    if (workouts.isEmpty()) {
                        sendUiEvent(UiEvent.ErrorOccured("Network Error!"))
                    } else {
                        sendUiEvent(UiEvent.Navigate(Routes.CREATE_WORKOUT_SELECT))
                    }
                }
                is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(
                    result.message ?: "Network Error!"))
            }
        } catch (e: Exception) {
            sendUiEvent(UiEvent.ErrorOccured(e.message ?: "An unexpected error occurred"))
        }
    }
    // Sends the selected workout in the background to the server
    fun postSelectedWorkout() {
        viewModelScope.launch {
            val selectedWorkout = _selectedWorkout.value
            val doWeekly = this@SharedCreateWorkoutViewModel.doWeekly

            if (selectedWorkout != null && doWeekly != 0) {
                val result = repository.postUserWorkout(SelectedWorkout(
                    do_weekly = doWeekly,
                    workout = selectedWorkout
                ))

                when (result) {
                    is Resource.Success -> {
                        sendUiEvent(UiEvent.Navigate(Routes.WORKOUT_ACTIVE))
                    }
                    is Resource.Error -> {
                        sendUiEvent(UiEvent.ErrorOccured(result.message ?: "An error occurred while posting the workout"))
                    }
                }
            } else {
                sendUiEvent(UiEvent.ErrorOccured("No workout selected"))
            }
        }
    }
}