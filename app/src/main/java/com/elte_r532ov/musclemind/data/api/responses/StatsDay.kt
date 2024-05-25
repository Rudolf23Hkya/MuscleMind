package com.elte_r532ov.musclemind.data.api.responses

data class StatsDay(
    val dailyReportData: List<DailyReportData>,
    val date: String
)