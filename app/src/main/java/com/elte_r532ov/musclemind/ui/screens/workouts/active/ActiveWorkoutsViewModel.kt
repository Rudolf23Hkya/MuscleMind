package com.elte_r532ov.musclemind.ui.screens.workouts.active

import androidx.lifecycle.ViewModel
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.SessionManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope

@HiltViewModel
class ActiveWorkoutsViewModel @Inject constructor(
    private val repository: MuscleMindRepository,
    private val sessionManagement: SessionManagement
) : ViewModel() {
    private val sessionToken = sessionManagement.getSessionToken()

    
}