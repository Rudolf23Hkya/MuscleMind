package com.elte_r532ov.musclemind.data.workoutsAndExercises

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elte_r532ov.musclemind.data.ExperienceLevel

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val exerciseId: Long = 0,
    val name: String,
    val category: Category,
    val muscleGroup: MuscleGroup,
    val experienceLevel: ExperienceLevel,
    val duration: Int, // In sec
    val caloriesBurnt: Int,
    val drawablePicName: String
)
