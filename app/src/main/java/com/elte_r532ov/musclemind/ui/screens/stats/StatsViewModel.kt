package com.elte_r532ov.musclemind.ui.screens.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.Resource
import com.elte_r532ov.musclemind.data.api.responses.StatsDay
import com.elte_r532ov.musclemind.data.enums.StatsFormat
import com.elte_r532ov.musclemind.data.enums.StatsMode
import com.elte_r532ov.musclemind.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: MuscleMindRepository
) : ViewModel() {

    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate

    private val _statsDayStats = MutableStateFlow<List<StatsDay>>(emptyList())
    val statsDayStats: StateFlow<List<StatsDay>> = _statsDayStats

    private val _scaledKcalStats = MutableStateFlow<List<Int>>(listOf(0, 0, 0, 0, 0, 0, 0))
    val scaledKcalStats: StateFlow<List<Int>> = _scaledKcalStats

    private val _realKcalStats = MutableStateFlow<List<Int>>(listOf(0, 0, 0, 0, 0, 0, 0))
    val realKcalStats: StateFlow<List<Int>> = _realKcalStats

    private val _scaledTimeStats = MutableStateFlow<List<Int>>(listOf(0, 0, 0, 0, 0, 0, 0))
    val scaledTimeStats: StateFlow<List<Int>> = _scaledTimeStats

    private val _realTimeStats = MutableStateFlow<List<Int>>(listOf(0, 0, 0, 0, 0, 0, 0))
    val realTimeStats: StateFlow<List<Int>> = _realTimeStats

    private val _selectedMode = MutableStateFlow(StatsMode.KCAL)
    val selectedMode: StateFlow<StatsMode> = _selectedMode

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        fetchStatsForDate(LocalDate.now())
    }

    private fun fetchStatsForDate(date: LocalDate) {
        viewModelScope.launch {
            when (val result = repository.getStats(date.year, date.monthValue, date.dayOfMonth)) {
                is Resource.Success -> {
                    _statsDayStats.value = result.data?.days ?: ArrayList()
                    loadReceivedData()
                }
                is Resource.Error -> {
                    sendUiEvent(UiEvent.ErrorOccured(result.message!!))
                }
            }
        }
    }

    fun nextWeek() {
        _currentDate.value = _currentDate.value.plusWeeks(1)
        fetchStatsForDate(_currentDate.value)
    }

    fun previousWeek() {
        _currentDate.value = _currentDate.value.minusWeeks(1)
        fetchStatsForDate(_currentDate.value)
    }

    private fun loadReceivedData() {
        val statsDays = _statsDayStats.value

        val kcalStats = statsDays.map { day ->
            day.dailyReportData.sumOf { it.calories_burnt }
        }

        val timeStats = statsDays.map { day ->
            day.dailyReportData.sumOf { it.time_working_out_sec }
        }

        val maxKcal = kcalStats.maxOrNull() ?: 1
        val maxTime = timeStats.maxOrNull() ?: 1

        val normalizedKcalStats = kcalStats.map { (it.toFloat() / maxKcal * 150).toInt().coerceIn(0, 150) }
        val normalizedTimeStats = timeStats.map { (it.toFloat() / maxTime * 150).toInt().coerceIn(0, 150) }

        _scaledKcalStats.value = normalizedKcalStats
        _realKcalStats.value = kcalStats

        _scaledTimeStats.value = normalizedTimeStats
        _realTimeStats.value = timeStats
    }

    fun setMode(mode: StatsMode) {
        _selectedMode.value = mode
        loadReceivedData() // Reload data based on the selected mode
    }

    fun sendStatsByEmail(format: StatsFormat) {
        viewModelScope.launch {
            if (format == StatsFormat.CSV)
                repository.getStatsViaEmail(csv = true, pdf = false)
            else if (format == StatsFormat.PDF)
                repository.getStatsViaEmail(csv = false, pdf = true)
        }
        sendUiEvent(UiEvent.ShowSnackbar("Your e-mail will arrive soon!"))
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}


