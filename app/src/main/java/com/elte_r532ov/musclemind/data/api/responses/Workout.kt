package com.elte_r532ov.musclemind.data.api.responses

import com.elte_r532ov.musclemind.data.enums.Category
import com.elte_r532ov.musclemind.data.enums.ExperienceLevel
import com.elte_r532ov.musclemind.data.enums.MuscleGroup

data class Workout(
    val category: List<Category>,
    val drawablepicname: String,
    val exercise_order: List<Int>,
    val exercises: List<Exercise>,
    val experiencelevel: ExperienceLevel,
    val musclegroup: List<MuscleGroup>,
    val name: String,
    val workoutid: Int,
    val userWorkoutId: Int = 0 // Default is 0, only used for active Workouts
)