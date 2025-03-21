package com.elte_r532ov.musclemind.ui.screens.workouts.active

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.api.Resource
import com.elte_r532ov.musclemind.data.api.responses.Exercise
import com.elte_r532ov.musclemind.data.api.responses.ExerciseDone
import com.elte_r532ov.musclemind.data.api.responses.UserWorkout
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.data.api.responses.WorkoutDone
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.UiEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.LinkedList

@HiltViewModel
class SharedActiveWorkoutViewModel @Inject constructor(
    private val repository: MuscleMindRepository,
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

    private val _activeWorkouts = MutableLiveData<List<Workout>>()
    val activeWorkouts: LiveData<List<Workout>> = _activeWorkouts

    // Selected workout
    private val _selectedWorkout = MutableLiveData<Workout?>()

    // Selected exercises
    private val _selectedExercises = MutableLiveData<List<Exercise>>()
    val selectedExercises: LiveData<List<Exercise>> = _selectedExercises

    private val _selectedExerciseOrdered = MutableLiveData<LinkedList<Exercise>>()

    /// Exercise in progress ///
    private val _exerciseName = MutableLiveData("")
    val exerciseName: LiveData<String> = _exerciseName

    private val _imageUrl = MutableLiveData("")
    val imageUrl: LiveData<String> = _imageUrl

    private val _isNextButtonEnabled = MutableLiveData(false)
    val isNextButtonEnabled: LiveData<Boolean> = _isNextButtonEnabled

    private val _reps = MutableLiveData(0)
    val reps: LiveData<Int> = _reps

    private val _isRepsZero = MutableLiveData(true)
    val isRepsZero: LiveData<Boolean> = _isRepsZero

    // TIMER
    private val _remainingTime = MutableLiveData(0)
    val remainingTime: LiveData<Int> = _remainingTime

    private val _maxTime = MutableLiveData(1) // Default to 1 to avoid division by zero

    private val _progress = MutableLiveData(1f)
    val progress: LiveData<Float> = _progress

    private val _elapsedTime = MutableLiveData(0)

    private var timer: CountDownTimer? = null
    private var countUpTimer: Job? = null


    // For collecting data to post after the exercise
    private val _exercisesDone = MutableLiveData<MutableList<ExerciseDone>>(mutableListOf())
    val exercisesDone: LiveData<MutableList<ExerciseDone>> get() = _exercisesDone


    init {
        viewModelScope.launch {
            _userName.value = echoUserName(repository)

            when (val result = repository.updateAccessToken()) {
                is Resource.Success -> {
                    _activeWorkouts.value = emptyList()

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

    private fun addExerciseDone(exercise: Exercise, skipped: Boolean) {
        // If the exercise is duration based then duration - remainingTime will be added for duration
        // If the exercise is reps based the elapsed time will be added as duration, because
        // It took that much time for the user to complete the exercise
        val duration = if (exercise.duration == 0) {
            _elapsedTime.value ?: 0
        } else {
            exercise.duration - (_remainingTime.value ?: 0)
        }
        val exerciseDone = ExerciseDone(
            skipped = skipped,
            duration = duration,
            rating = 3,
            cal = exercise.caloriesburnt,
            name = exercise.name
        )
        _exercisesDone.value?.add(exerciseDone)
    }

    fun onExerciseNext() {
        val exerciseOrder = _selectedExerciseOrdered.value
        if (!exerciseOrder.isNullOrEmpty()) {
            val currentExercise = exerciseOrder.removeFirst()
            addExerciseDone(currentExercise, skipped = false)
            if (exerciseOrder.isNotEmpty()) {
                val nextExercise = exerciseOrder.first()
                updateCurrentExercise(nextExercise)
            } else {
                completeWorkout()
            }
        }
    }

    fun onExerciseSkipped() {
        val exerciseOrder = _selectedExerciseOrdered.value
        if (!exerciseOrder.isNullOrEmpty()) {
            val currentExercise = exerciseOrder.removeFirst()
            addExerciseDone(currentExercise, skipped = true)
            if (exerciseOrder.isNotEmpty()) {
                val nextExercise = exerciseOrder.first()
                updateCurrentExercise(nextExercise)
            } else {
                completeWorkout()
            }
        }
    }

    private fun completeWorkout() {
        countUpTimer?.cancel()
        sendUiEvent(UiEvent.Navigate(Routes.WORKOUT_RATING))
    }

    private fun updateCurrentExercise(exercise: Exercise?) {
        if (exercise != null) {
            _exerciseName.value = exercise.name
            _imageUrl.value = exercise.drawablepicname
            _remainingTime.value = exercise.duration
            _maxTime.value = exercise.duration
            _reps.value = exercise.reps
            _isRepsZero.value = exercise.reps == 0
            _isNextButtonEnabled.value = false
            if (exercise.duration == 0) {
                startCountUpTimer()
            } else {
                startTimer(exercise.duration)
            }
        }
    }

    private fun startTimer(duration: Int) {
        timer?.cancel()
        countUpTimer?.cancel()
        _maxTime.value = duration
        _progress.value = 1f
        timer = object : CountDownTimer(duration.toLong() * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                _remainingTime.value = secondsRemaining
                _progress.value = secondsRemaining.toFloat() / _maxTime.value!!.toFloat()
                _isNextButtonEnabled.value = _progress.value == 0f
            }

            override fun onFinish() {
                _remainingTime.value = 0
                _progress.value = 0f
                _isNextButtonEnabled.value = true
            }
        }.start()
    }

    private fun startCountUpTimer() {
        timer?.cancel()
        countUpTimer?.cancel()
        _elapsedTime.value = 0
        _progress.value = 0f
        countUpTimer = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedTime.value = (_elapsedTime.value ?: 0) + 1
            }
        }
    }
    // For saving the ratings
    fun updateRating(index: Int, newRating: Int) {
        _exercisesDone.value?.let {
            if (index >= 0 && index < it.size) {
                val updatedExercise = it[index].copy(rating = newRating)
                it[index] = updatedExercise
                _exercisesDone.value = it
            }
        }
    }
    fun saveWorkout(){
        val workoutDoneData = _selectedWorkout.value?.let {
            exercisesDone.value?.let { it1 ->
                WorkoutDone(exercises = it1,
                    it.userWorkoutId)
            }
        }
        Log.d("RATING", "Data: ${workoutDoneData.toString()}")
        if(workoutDoneData != null) {
            sendUiEvent(UiEvent.Navigate(Routes.WORKOUT_ACTIVE))
            viewModelScope.launch {
                try {
                    repository.workoutDone(workoutDoneData)
                } catch (e: Exception) {
                    //Error handling
                    sendUiEvent(UiEvent.ErrorOccured("Failed to save workout!"))
                }
            }
        }
        else
            sendUiEvent(UiEvent.ErrorOccured("Unexpected error occurred!"))
    }


    private suspend fun getActiveWorkouts() {
        try {
            when (val result = repository.getUserWorkout()) {
                is Resource.Success -> {
                    val workouts = result.data ?: emptyList()
                    if (workouts.isEmpty()) {
                        sendUiEvent(UiEvent.ErrorOccured("You have no active Workouts."))
                    }
                    else{
                        try {
                            _activeWorkouts.value = processWorkouts(workouts)
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
                userWorkoutId = listOfWorkouts[i].id, // This makes the workout a unique user Workout
                weekly = listOfWorkouts[i].do_weekly
            ))
        }
        return processedWorkouts
    }


    private suspend fun echoUserName(repository : MuscleMindRepository) : String{
        return repository.getUserData().data?.username ?: ""
    }
}

