package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.CaloriesData
import com.elte_r532ov.musclemind.data.api.responses.FullAutUserData
import com.elte_r532ov.musclemind.data.api.responses.LoginData
import com.elte_r532ov.musclemind.data.api.responses.SelectedWorkout
import com.elte_r532ov.musclemind.data.api.responses.Success
import com.elte_r532ov.musclemind.data.api.responses.Tokens
import com.elte_r532ov.musclemind.data.api.responses.UserWorkout
import com.elte_r532ov.musclemind.data.api.responses.WeekStats
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.data.api.responses.WorkoutDone
import com.elte_r532ov.musclemind.util.Resource
import org.json.JSONException
import org.json.JSONObject

class MuscleMindRepoImplApi(
    private val apiDao: ApiDao,
    private val sessionManagement: SessionManagement
    )
    : MuscleMindRepository {

    override suspend fun registerUser(ud: UserData): Resource<FullAutUserData> {
        return try {
            val regResponse = apiDao.register(ud)
            if (regResponse.isSuccessful) {
                // Checking if the body is not null, then return it wrapped in Resource.Success
                regResponse.body()?.let { fullAutUserData ->
                    // Saving the tokens
                    sessionManagement.saveTokens(fullAutUserData.tokens.access, fullAutUserData.tokens.refresh)
                    // Caching user data
                    sessionManagement.storeUserData(fullAutUserData.userData)

                    Resource.Success(fullAutUserData)
                } ?: Resource.Error("No data recived from the server!", null)
            } else {
                // The API responded but indicated a failure
                val errorStr = regResponse.errorBody()?.string()
                if (errorStr != null) {
                    try {
                        Resource.Error(JSONObject(errorStr).getString("error"), null)
                    } catch (e: JSONException) {
                        Resource.Error(regResponse.errorBody()?.string() ?: "Registration failed with unknown error", null)
                    }
                } else {
                    Resource.Error(regResponse.errorBody()?.string() ?: "Registration failed with unknown error", null)
                }
            }
        } catch (e: Exception) {
            // Handle network exceptions
            Resource.Error(e.message ?: "Network error!", null)
        }
    }

    override suspend fun getAccessToken(refreshToken: String): Resource<Tokens> {
        TODO("Not yet implemented")
    }

    override suspend fun getCalories(authToken: String): Resource<CaloriesData> {
        TODO("Not yet implemented")
    }

    override suspend fun addCalories(
        caloriesData: CaloriesData,
        authToken: String
    ): Resource<CaloriesData> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecomWorkouts(authToken: String): Resource<List<Workout>> {
        TODO("Not yet implemented")
    }

    override suspend fun postUserWorkout(
        workoutData: SelectedWorkout,
        authToken: String
    ): Resource<SelectedWorkout> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserWorkout(authToken: String): Resource<List<UserWorkout>> {
        TODO("Not yet implemented")
    }

    override suspend fun workoutDone(
        workoutDoneData: WorkoutDone,
        authToken: String
    ): Resource<WorkoutDone> {
        TODO("Not yet implemented")
    }

    override suspend fun getStats(
        year: Int,
        month: Int,
        day: Int,
        authToken: String
    ): Resource<WeekStats> {
        TODO("Not yet implemented")
    }

    override suspend fun getStatsViaEmail(
        csv: Boolean,
        pdf: Boolean,
        authToken: String
    ): Resource<Success> {
        TODO("Not yet implemented")
    }

    override suspend fun loginAttempt(email: String, password: String): Resource<FullAutUserData> {
        return try {
            val loginResponse = apiDao.login(LoginData(email = email, password = password))
            if (loginResponse.isSuccessful) {
                loginResponse.body()?.let { fullAutUserData ->
                    // Saving the tokens
                    sessionManagement.saveTokens(fullAutUserData.tokens.access, fullAutUserData.tokens.refresh)
                    // Caching user data
                    sessionManagement.storeUserData(fullAutUserData.userData)

                    Resource.Success(fullAutUserData)
                } ?: Resource.Error("Login successful but no user data returned", null)
            } else {
                // The API responded but indicated a failure
                val errorStr = loginResponse.errorBody()?.string()
                if (errorStr != null) {
                    try {
                        Resource.Error(JSONObject(errorStr).getString("error"), null)
                    } catch (e: JSONException) {
                        Resource.Error(loginResponse.errorBody()?.string() ?: "Login failed with unknown error", null)
                    }
                } else {
                    Resource.Error(loginResponse.errorBody()?.string() ?: "Login failed with unknown error", null)
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions that occur during the network request
            Resource.Error(e.message ?: "Error occurred during login attempt", null)
        }
    }


    //This function returns user data from the cache
    override suspend fun getUserData(authToken: String): Resource<UserData> {
        return try {
            return Resource.Success(sessionManagement.getUserData())
        } catch (e: Exception) {
            // Handle any exceptions that might occur during the network request
            Resource.Error(e.message ?: "Exception occurred while fetching user data", null)
        }
    }
    override suspend fun deleteUserData(authToken: String) {
        TODO("Not yet implemented")
    }

}