package com.elte_r532ov.musclemind.data.api.responses

data class StatsWeek(
    val date: String,
    val days: List<StatsDay>
)