package com.elte_r532ov.musclemind.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.enums.StatsFormat
import com.elte_r532ov.musclemind.data.enums.StatsMode
import com.elte_r532ov.musclemind.data.workoutsAndExercises.WorkoutExcRepository
import com.elte_r532ov.musclemind.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: MuscleMindRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
        }
    }
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // Values should not be more than 150 and less than 0
    private val kcalStats = listOf(150, 20, 70, 60, 90, 80, 50)
    private val timeStats = listOf(-20, 10, 40, 35, 60, 45, 25)

    private val _scaledStats = MutableStateFlow(kcalStats)
    val scaledStats: StateFlow<List<Int>> = _scaledStats

    private val _realStats = MutableStateFlow(kcalStats)
    val realStats: StateFlow<List<Int>> = _realStats

    private val _selectedMode = MutableStateFlow(StatsMode.KCAL)
    val selectedMode: StateFlow<StatsMode> = _selectedMode

    fun setMode(mode: StatsMode) {
        _selectedMode.value = mode
        _scaledStats.value = when (mode) {
            StatsMode.KCAL -> kcalStats
            StatsMode.TIME -> timeStats
        }
        _realStats.value = when (mode) {
            StatsMode.KCAL -> kcalStats
            StatsMode.TIME -> timeStats
        }
    }

    fun sendStatsByEmail(format: StatsFormat) {
        // Implement email sending functionality
    }
    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
