package com.elte_r532ov.musclemind.data

import com.elte_r532ov.musclemind.data.api.Resource
import com.elte_r532ov.musclemind.data.api.responses.CaloriesData
import com.elte_r532ov.musclemind.data.api.responses.Disease
import com.elte_r532ov.musclemind.data.api.responses.FullAutUserData
import com.elte_r532ov.musclemind.data.api.responses.Tokens
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.data.api.responses.SelectedWorkout
import com.elte_r532ov.musclemind.data.api.responses.Success
import com.elte_r532ov.musclemind.data.api.responses.UserWorkout
import com.elte_r532ov.musclemind.data.api.responses.WeekStats
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.data.api.responses.WorkoutDone

//This is an abstraction for transferring data from and to the app
interface MuscleMindRepository {

    suspend fun registerUser(ud : UserData,d : Disease): Resource<FullAutUserData>

    suspend fun loginAttempt(email: String, password: String): Resource<FullAutUserData>

    suspend fun getUserData(): Resource<UserData>

    suspend fun updateAccessToken(): Resource<Tokens>

    suspend fun getCalories(): Resource<CaloriesData>

    suspend fun addCalories(caloriesData: CaloriesData): Resource<String>

    suspend fun getRecomWorkouts(weightlifting: Boolean, trx: Boolean): Resource<List<Workout>>

    suspend fun postUserWorkout(workoutData: SelectedWorkout): Resource<SelectedWorkout>

    suspend fun getUserWorkout(): Resource<List<UserWorkout>>

    suspend fun workoutDone(workoutDoneData: WorkoutDone): Resource<WorkoutDone>

    suspend fun getStats(year: Int, month: Int, day: Int): Resource<WeekStats>

    suspend fun getStatsViaEmail(csv: Boolean, pdf: Boolean): Resource<String>

}