package com.elte_r532ov.musclemind.data.api.responses

data class WorkoutDone(
    val exercises: List<ExerciseDone>,
    val user_workout_id: Int
)