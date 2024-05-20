package com.elte_r532ov.musclemind.data.api.responses

data class Exercise(
    val caloriesburnt: Int,
    val category: String,
    val drawablepicname: String,
    val duration: Int,
    val exerciseid: Int,
    val experiencelevel: String,
    val musclegroup: String,
    val name: String,
    val reps: Int
)