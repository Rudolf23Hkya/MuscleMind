package com.elte_r532ov.musclemind.data.api.responses

import com.elte_r532ov.musclemind.data.enums.Category
import com.elte_r532ov.musclemind.data.enums.ExperienceLevel
import com.elte_r532ov.musclemind.data.workoutsAndExercises.MuscleGroup

data class WorkoutApi(
    val category: List<Category>,
    val drawablepicname: String,
    val exercise_order: List<Int>,
    val exerciseApis: List<ExerciseApi>,
    val experiencelevel: ExperienceLevel,
    val musclegroup: List<MuscleGroup>,
    val name: String,
    val workoutid: Int
)