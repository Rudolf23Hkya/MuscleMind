package com.elte_r532ov.musclemind.ui.screens.workouts.active

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elte_r532ov.musclemind.data.userData.MuscleMindRepository
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Workout
import com.elte_r532ov.musclemind.data.workoutsAndExercises.WorkoutExcRepository
import com.elte_r532ov.musclemind.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ActiveWorkoutsViewModel @Inject constructor(
    private val userRepo: MuscleMindRepository,
    private val workoutRepo : WorkoutExcRepository,
    private val sessionManagement: SessionManagement
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _userName = MutableLiveData<String>()
    val userNameLiveData: LiveData<String> = _userName

    private val _activeWorkouts = MutableLiveData<Workout>()
    val activeWorkoutLiveData: LiveData<Workout> = _activeWorkouts

    init {
        viewModelScope.launch {
            _userName.value = echoUserName(userRepo,sessionManagement)
            //_activeWorkouts.value = workoutRepo.

            _uiEvent.send(UiEvent.ShowSnackbar("Hello $_userName.value"))
        }
    }




    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

suspend fun echoUserName(repository : MuscleMindRepository, sessionManagement : SessionManagement) : String{
    val sessionToken = sessionManagement.getSessionToken()

    return repository.getUserBySessionToken(sessionToken!!)?.name ?: "UserNotFound"
}

