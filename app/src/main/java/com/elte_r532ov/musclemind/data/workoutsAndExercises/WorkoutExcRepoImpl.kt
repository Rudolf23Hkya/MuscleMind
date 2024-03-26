package com.elte_r532ov.musclemind.data.workoutsAndExercises

class WorkoutExcRepoImpl(private val dao: WorkoutDao) : WorkoutExcRepository {
    override suspend fun insertWorkout(workout: Workout) {
        dao.insertWorkout(workout)
    }

    override suspend fun insertExercise(exercise: Exercise) {
        dao.insertExercise(exercise)
    }

    override suspend fun insertWorkoutExerciseCrossRef(workoutId: Long,exerciseId :Long) {
        val crossRef = WorkoutExerciseCrossRef(workoutId,exerciseId)
        dao.insertWorkoutExerciseCrossRef(crossRef)
    }

    override suspend fun getWorkoutWithExercises(workoutId: Long): List<WorkoutWithExercises> {
        return dao.getWorkoutWithExercises(workoutId)
    }

    override suspend fun getExerciseWithWorkouts(exerciseId: Long): List<ExerciseWithWorkouts> {
        return  dao.getExerciseWithWorkouts(exerciseId)
    }
}