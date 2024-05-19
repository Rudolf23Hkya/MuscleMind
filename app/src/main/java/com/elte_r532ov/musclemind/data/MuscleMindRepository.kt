package com.elte_r532ov.musclemind.data

import com.elte_r532ov.musclemind.data.api.responses.CaloriesData
import com.elte_r532ov.musclemind.data.api.responses.FullAutUserData
import com.elte_r532ov.musclemind.data.api.responses.LoginData
import com.elte_r532ov.musclemind.data.api.responses.Tokens
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.data.api.responses.SelectedWorkout
import com.elte_r532ov.musclemind.data.api.responses.Success
import com.elte_r532ov.musclemind.data.api.responses.UserWorkout
import com.elte_r532ov.musclemind.data.api.responses.WeekStats
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.data.api.responses.WorkoutDone
import com.elte_r532ov.musclemind.util.Resource

//This is an abstraction for transferring data from and to the app
interface MuscleMindRepository {

    suspend fun registerUser(ud : UserData):Resource<FullAutUserData>

    suspend fun loginAttempt(email: String, password: String): Resource<FullAutUserData>

    suspend fun getUserData(authToken: String): Resource<UserData>

    suspend fun getAccessToken(refreshToken: String): Resource<Tokens>

    suspend fun getCalories(authToken: String): Resource<CaloriesData>

    suspend fun addCalories(caloriesData: CaloriesData, authToken: String): Resource<CaloriesData>

    suspend fun getRecomWorkouts(authToken: String): Resource<List<Workout>>

    suspend fun postUserWorkout(workoutData: SelectedWorkout, authToken: String): Resource<SelectedWorkout>

    suspend fun getUserWorkout(authToken: String): Resource<List<UserWorkout>>

    suspend fun workoutDone(workoutDoneData: WorkoutDone, authToken: String): Resource<WorkoutDone>

    suspend fun getStats(year: Int, month: Int, day: Int, authToken: String): Resource<WeekStats>

    suspend fun getStatsViaEmail(csv: Boolean, pdf: Boolean, authToken: String): Resource<Success>

    suspend fun deleteUserData(authToken: String)
}