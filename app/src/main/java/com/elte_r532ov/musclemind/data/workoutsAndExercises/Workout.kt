package com.elte_r532ov.musclemind.data.workoutsAndExercises

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elte_r532ov.musclemind.data.userData.ExperienceLevel

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true) val workoutId: Long = 0,
    val name: String,
    val experienceLevel: ExperienceLevel,
    val drawablePicName: String,
    val listOfExercises: List<Long>
)