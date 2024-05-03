package com.elte_r532ov.musclemind.data.workoutsAndExercises

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elte_r532ov.musclemind.data.RoomDBConverters

@Database(entities = [
    Exercise::class, Workout::class,
    WorkoutExerciseCrossRef::class
                     ], version = 3, exportSchema = false)
@TypeConverters(RoomDBConverters::class)
abstract class WorkoutExerciseDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}