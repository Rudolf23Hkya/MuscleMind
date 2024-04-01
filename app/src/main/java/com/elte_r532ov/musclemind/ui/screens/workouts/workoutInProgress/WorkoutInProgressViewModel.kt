package com.elte_r532ov.musclemind.ui.screens.workouts.workoutInProgress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Exercise
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Workout
import com.elte_r532ov.musclemind.data.workoutsAndExercises.WorkoutExcRepository
import com.elte_r532ov.musclemind.util.Routes
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutInProgressViewModel @Inject constructor
    (private val workoutRepo : WorkoutExcRepository):ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _selectedExercise = MutableLiveData<Exercise>()
    val selectedExercise: LiveData<Exercise> = _selectedExercise

    private lateinit var workoutInDetail : Workout
    private lateinit var exerciseIds : List<Long>

    private var workoutId = 0L
    private var curExerciseId = 0

    private var done = 0
    private var skipped = 0

    fun nextExercise(){
        this.curExerciseId++
        done++
        if(curExerciseId < exerciseIds.size)
            updateExercise(exerciseIds[this.curExerciseId])
        else
            sendUiEvent(UiEvent.Navigate(Routes.WORKOUTS_ACTIVE))
    }
    fun skippedExercise(){
        this.curExerciseId++
        skipped++
        if(curExerciseId < exerciseIds.size)
            updateExercise(exerciseIds[this.curExerciseId])
        else
            sendUiEvent(UiEvent.Navigate(Routes.WORKOUTS_ACTIVE))
    }
    private fun updateExercise(curExerciseId : Long){
        viewModelScope.launch {
            _selectedExercise.postValue(workoutRepo.getExerciseWithId(curExerciseId))
        }
    }

    fun initWorkoutId(workoutId: Long){
        //This only needs to be initialized for the first time
        if(this.workoutId == 0L) {
            this.workoutId = workoutId

            viewModelScope.launch {
                workoutInDetail = workoutRepo.getWorkoutWithID(workoutId)
                exerciseIds = workoutInDetail.listOfExercises
            }
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}