package com.elte_r532ov.musclemind.ui.screens.workouts.active

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ActiveWorkoutsViewModel @Inject constructor(
    private val userRepo: MuscleMindRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _userName = MutableLiveData<String>()
    val userNameLiveData: LiveData<String> = _userName

    private val _activeWorkouts = MutableLiveData<List<Workout>>()
    val activeWorkoutLiveData: LiveData<List<Workout>> = _activeWorkouts


    init {
        viewModelScope.launch {
            _userName.value = echoUserName(userRepo)
            //TODO
            //val workouts = workoutRepo.getWorkouts()
            //_activeWorkouts.value = workouts
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
    private suspend fun echoUserName(repository : MuscleMindRepository) : String{

        return repository.getUserData().data?.username ?: ""
    }
}

