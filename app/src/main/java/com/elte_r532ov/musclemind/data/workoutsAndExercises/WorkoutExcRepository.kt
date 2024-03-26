package com.elte_r532ov.musclemind.data.workoutsAndExercises

//This is an abstraction for transferring data from and to the app
interface WorkoutExcRepository {

    suspend fun insertWorkout(workout: Workout)


    suspend fun insertExercise(exercise: Exercise)


    suspend fun insertWorkoutExerciseCrossRef(workoutId: Long,exerciseId :Long)

    suspend fun getWorkoutWithExercises(workoutId: Long): List<WorkoutWithExercises>

    suspend fun getExerciseWithWorkouts(exerciseId: Long): List<ExerciseWithWorkouts>
}