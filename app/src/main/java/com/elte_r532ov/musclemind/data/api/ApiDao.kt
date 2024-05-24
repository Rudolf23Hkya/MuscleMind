package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.api.responses.CaloriesData
import com.elte_r532ov.musclemind.data.api.responses.FullAutUserData
import com.elte_r532ov.musclemind.data.api.responses.LoginData
import com.elte_r532ov.musclemind.data.api.responses.RegisterUser
import com.elte_r532ov.musclemind.data.api.responses.Success
import com.elte_r532ov.musclemind.data.api.responses.Tokens
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.data.api.responses.UserWorkout
import com.elte_r532ov.musclemind.data.api.responses.WeekStats
import com.elte_r532ov.musclemind.data.api.responses.WorkoutDone
import com.elte_r532ov.musclemind.data.api.responses.SelectedWorkout
import com.elte_r532ov.musclemind.data.api.responses.Workout
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiDao {
    @POST("loginUser/")
    suspend fun login(@Body loginRequest: LoginData): Response<FullAutUserData>

    @POST("regUser/")
    suspend fun register(@Body regUserData: RegisterUser): Response<FullAutUserData>

    @POST("access_token/")
    suspend fun getAccessToken(@Body tokens: Tokens): Response<Tokens>

    @GET("get_user_data/")
    suspend fun getUserData(@Header("Authorization") authToken: String): Response<UserData>

    @GET("get_calories/")
    suspend fun getCalories(@Header("Authorization") authToken: String): Response<CaloriesData>

    @POST("add_calories/")
    suspend fun addCalories(@Body caloriesData: CaloriesData, @Header("Authorization") authToken: String): Response<String>

    @GET("get_recom_workouts/")
    suspend fun getRecomWorkouts(
        @Header("Authorization") authToken: String,
        @Query("weightlifting") weightlifting: Boolean,
        @Query("trx") trx: Boolean
    ): Response<List<Workout>>

    @POST("post_user_workout/")
    suspend fun postUserWorkout(@Body workoutData: SelectedWorkout, @Header("Authorization") authToken: String): Response<SelectedWorkout>

    @GET("get_user_workout/")
    suspend fun getUserWorkout(@Header("Authorization") authToken: String): Response<UserWorkout>

    @POST("workout_done/")
    suspend fun workoutDone(@Body workoutDoneData: WorkoutDone, @Header("Authorization") authToken: String): Response<WorkoutDone>

    @GET("get_stats/")
    suspend fun getStats(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Header("Authorization") authToken: String
    ): Response<WeekStats>

    @GET("get_stats_via_email/")
    suspend fun getStatsViaEmail(
        @Query("csv") csv: Boolean,
        @Query("pdf") pdf: Boolean,
        @Header("Authorization") authToken: String
    ): Response<Success>
}

