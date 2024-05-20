package com.elte_r532ov.musclemind.ui.screens.workouts.workoutInDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.Exercise
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutInDetailSharedViewModel @Inject constructor(
    private val repository: MuscleMindRepository
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private lateinit var workoutInDetail : Workout
    private lateinit var exerciseIds : List<Long>

    private val _selectedExercises = MutableLiveData<List<Exercise>>()
    val selectedExercises: LiveData<List<Exercise>> = _selectedExercises


    fun initWorkoutId(workoutId: Long){

        viewModelScope.launch {
            //TODO
            //workoutInDetail = workoutRepo.getWorkoutWithID(workoutId)
            //exerciseIds = workoutInDetail.listOfExercises
            val exercisesTemp = mutableListOf<Exercise>()

            for (value in exerciseIds) {
                //exercisesTemp.add(workoutRepo.getExerciseWithId(value))
            }
            _selectedExercises.value = exercisesTemp.toList()
        }
    }



    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}