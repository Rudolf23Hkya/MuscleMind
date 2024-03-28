package com.elte_r532ov.musclemind.ui.screens.workouts.workoutInDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Exercise
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Workout
import com.elte_r532ov.musclemind.data.workoutsAndExercises.WorkoutExcRepository
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates


@HiltViewModel
class WorkoutInDetailViewModel @Inject constructor(
    private val workoutRepo : WorkoutExcRepository,
    private val sessionManagement: SessionManagement
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private lateinit var workoutInDetail : Workout
    private lateinit var exerciseIds : List<Long>

    private val _selectedExercises = MutableLiveData<List<Exercise>>()
    val selectedExercises: LiveData<List<Exercise>> = _selectedExercises


    fun initWorkoutId(workoutId: Long){

        viewModelScope.launch {
            workoutInDetail = workoutRepo.getWorkoutWithID(workoutId)
            exerciseIds = workoutInDetail.listOfExercises
            val exercisesTemp = mutableListOf<Exercise>()

            for (value in exerciseIds) {
                exercisesTemp.add(workoutRepo.getExerciseWithId(value))
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