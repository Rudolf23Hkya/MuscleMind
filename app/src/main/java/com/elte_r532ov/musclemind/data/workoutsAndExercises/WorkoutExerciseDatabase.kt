package com.elte_r532ov.musclemind.data.workoutsAndExercises

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elte_r532ov.musclemind.data.EnumConverters

@Database(entities = [
    Exercise::class, Workout::class,
    WorkoutExerciseCrossRef::class
                     ], version = 1, exportSchema = false)
@TypeConverters(EnumConverters::class)
abstract class WorkoutExerciseDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}