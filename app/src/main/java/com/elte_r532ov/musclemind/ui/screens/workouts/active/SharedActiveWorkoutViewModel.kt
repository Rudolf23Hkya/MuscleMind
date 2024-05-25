package com.elte_r532ov.musclemind.ui.screens.workouts.active

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.api.Resource
import com.elte_r532ov.musclemind.data.api.responses.Exercise
import com.elte_r532ov.musclemind.data.api.responses.UserWorkout
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.LinkedList

@HiltViewModel
class SharedActiveWorkoutViewModel @Inject constructor(
    private val repository: MuscleMindRepository
) : ViewModel() {
    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    //// Data for the views////
    private val _userName = MutableLiveData<String>()
    val userNameLiveData: LiveData<String> = _userName

    private val _doWeekly = MutableLiveData<List<Int>>()
    val doWeekly: LiveData<List<Int>> = _doWeekly

    private val _activeWorkouts = MutableLiveData<List<Workout>>()
    val activeWorkouts: LiveData<List<Workout>> = _activeWorkouts

    // Selected workout
    private val _selectedWorkout = MutableLiveData<Workout?>()
    val selectedWorkout: LiveData<Workout?> = _selectedWorkout

    // Selected exercises
    private val _selectedExercises = MutableLiveData<List<Exercise>>()
    val selectedExercises: LiveData<List<Exercise>> = _selectedExercises

    private val _selectedExerciseOrdered = MutableLiveData<LinkedList<Exercise>>()

    /// Exercise in progress ///
    private val _exerciseName = MutableLiveData("")
    val exerciseName: LiveData<String> = _exerciseName

    private val _imageUrl = MutableLiveData("")
    val imageUrl: LiveData<String> = _imageUrl

    private val _remainingTime = MutableLiveData(0)
    val remainingTime: LiveData<Int> = _remainingTime


    init {
        viewModelScope.launch {
            _userName.value = echoUserName(repository)

            when (val result = repository.updateAccessToken()) {
                is Resource.Success -> {
                    _activeWorkouts.value = emptyList()
                    _doWeekly.value = emptyList()

                    getActiveWorkouts()
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
    fun onWorkoutStarted() {
        val exerciseOrder = _selectedWorkout.value?.exercise_order ?: listOf()
        val exercises = _selectedExercises.value ?: listOf()
        val orderedExercises = LinkedList<Exercise>()

        for (orderIndex in exerciseOrder) {
            val exercise = exercises.find { it.exerciseid == orderIndex }
            if (exercise != null) {
                orderedExercises.add(exercise)
            }
        }

        _selectedExerciseOrdered.value = orderedExercises

        if (orderedExercises.isNotEmpty()) {
            val firstExercise = orderedExercises.first
            updateCurrentExercise(firstExercise)
            sendUiEvent(UiEvent.Navigate(Routes.WORKOUT_IN_PROGRESS))
        }
    }


    fun onExerciseNext() {
        val exerciseOrder = _selectedExerciseOrdered.value
        if (!exerciseOrder.isNullOrEmpty()) {
            exerciseOrder.removeFirst()
            if (exerciseOrder.isNotEmpty()) {
                val nextExercise = exerciseOrder.first
                updateCurrentExercise(nextExercise)
            } else {
                completeWorkout()
            }
        }
    }

    fun onExerciseSkipped() {
        val exerciseOrder = _selectedExerciseOrdered.value
        if (!exerciseOrder.isNullOrEmpty()) {
            exerciseOrder.removeFirst()
            if (exerciseOrder.isNotEmpty()) {
                val nextExercise = exerciseOrder.first
                updateCurrentExercise(nextExercise)
            } else {
                completeWorkout()
            }
        }
    }
    private fun completeWorkout() {
        _exerciseName.value = "Completed"
        _imageUrl.value = ""
        _remainingTime.value = 0
        sendUiEvent(UiEvent.Navigate(Routes.WORKOUT_RATING))
    }

    private fun updateCurrentExercise(exercise: Exercise?) {
        if (exercise != null) {
            _exerciseName.value = exercise.name
            _imageUrl.value = exercise.drawablepicname
            _remainingTime.value = exercise.duration
        }
    }

    private suspend fun getActiveWorkouts() {
        try {
            when (val result = repository.getUserWorkout()) {
                is Resource.Success -> {
                    val workouts = result.data ?: emptyList()
                    if (workouts.isEmpty()) {
                        sendUiEvent(UiEvent.ErrorOccured("Network Error!"))
                    }
                    else{
                        try {
                            _activeWorkouts.value = processWorkouts(workouts)
                            _doWeekly.value = getWeekly(workouts)
                        }
                        catch (e: Exception){
                            sendUiEvent(UiEvent.ErrorOccured(e.message ?: "Invalid data received!"))
                        }
                    }
                }
                is Resource.Error -> sendUiEvent(UiEvent.ErrorOccured(
                    result.message ?: "Network Error!"))
            }
        } catch (e: Exception) {
            sendUiEvent(UiEvent.ErrorOccured(e.message ?: "An unexpected error occurred"))
        }
    }


    // Applying custom weights for and Exercises
    private fun processWorkouts(listOfWorkouts: List<UserWorkout>) : List<Workout>{

        val processedWorkouts = ArrayList<Workout>()
        // Iterate trough workouts
        for (i in listOfWorkouts.indices) {
            val customWeights = listOfWorkouts[i].weights
            val exercises = listOfWorkouts[i].workout.exercises
            val modifiedExercises = ArrayList<Exercise>()

            if(customWeights.size != exercises.size){
                throw IllegalArgumentException("Invalid data received!")
            }
            // Iterate trough Exercises
            for(j in customWeights.indices){
                if(exercises[j].reps != 0){
                    val exercise = Exercise(
                        caloriesburnt = (customWeights[j] * exercises[j].caloriesburnt).toInt(),
                        reps =  (customWeights[j] * exercises[j].reps).toInt(),
                        duration = exercises[j].duration, // duration is 0 here
                        category = exercises[j].category,
                        drawablepicname = exercises[j].drawablepicname,
                        exerciseid = exercises[j].exerciseid,
                        experiencelevel = exercises[j].experiencelevel,
                        musclegroup = exercises[j].musclegroup,
                        name = exercises[j].name
                    )
                    modifiedExercises.add(exercise)
                }
                else if(exercises[j].duration != 0){
                    val exercise = Exercise(
                        caloriesburnt = (customWeights[j] * exercises[j].caloriesburnt).toInt(),
                        reps =  exercises[j].reps,// reps is 0 here
                        duration = (customWeights[j] * exercises[j].duration).toInt(),
                        category = exercises[j].category,
                        drawablepicname = exercises[j].drawablepicname,
                        exerciseid = exercises[j].exerciseid,
                        experiencelevel = exercises[j].experiencelevel,
                        musclegroup = exercises[j].musclegroup,
                        name = exercises[j].name
                    )
                    modifiedExercises.add(exercise)
                }
                else{
                    throw IllegalArgumentException("Invalid data received!")
                }
            }
            processedWorkouts.add(Workout(
                category = listOfWorkouts[i].workout.category,
                drawablepicname = listOfWorkouts[i].workout.drawablepicname,
                exercise_order = listOfWorkouts[i].workout.exercise_order,
                exercises = modifiedExercises,
                experiencelevel = listOfWorkouts[i].workout.experiencelevel,
                musclegroup = listOfWorkouts[i].workout.musclegroup,
                name = listOfWorkouts[i].workout.name,
                workoutid = listOfWorkouts[i].workout.workoutid,
                userWorkoutId = listOfWorkouts[i].id // This makes the workout a unique user Workout
            ))
        }
        return processedWorkouts
    }
    private fun getWeekly(listOfWorkouts: List<UserWorkout>): List<Int>{
        val weeklyTemp = ArrayList<Int>()
        for (workout in listOfWorkouts) {
            weeklyTemp.add(workout.do_weekly)
        }
        return weeklyTemp
    }


    private suspend fun echoUserName(repository : MuscleMindRepository) : String{
        return repository.getUserData().data?.username ?: ""
    }
}

