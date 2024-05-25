package com.elte_r532ov.musclemind.data.api.responses

//Needed for showing statistics
data class DailyReportData(
    val calorie_intake: Int,
    val calories_burnt: Int,
    val date: String,
    val id: Int,
    val time_working_out_sec: Int,
    val user_id: Int
)