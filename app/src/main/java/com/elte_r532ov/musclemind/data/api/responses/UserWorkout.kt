package com.elte_r532ov.musclemind.data.api.responses

data class UserWorkout(
    val do_weekly: Int,
    val id: Int,
    val weights: List<Double>,
    val workout: Workout
)