package com.elte_r532ov.musclemind.data.api
import android.content.Context
import android.content.Intent
import com.elte_r532ov.musclemind.MainActivity
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.CaloriesData
import com.elte_r532ov.musclemind.data.api.responses.Disease
import com.elte_r532ov.musclemind.data.api.responses.FullAutUserData
import com.elte_r532ov.musclemind.data.api.responses.LoginData
import com.elte_r532ov.musclemind.data.api.responses.RegisterUser
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
import retrofit2.Response

class MuscleMindRepoImplApi(
    private val apiDao: ApiDao,
    private val sessionManagement: SessionManagement,
    private val context: Context
    )
    : MuscleMindRepository {
        // If the refresh expires the user needs to Log-in again
        private fun restartMainActivity() {
            //First the tokens need to be deleted
            sessionManagement.deleteTokens()
            //Main activity restart
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
        // Generic Api handler
        private fun <T> handleApiResponse(response: Response<T>, onSuccess: (T) -> Resource<T>): Resource<T> {
            return if (response.isSuccessful) {
                response.body()?.let {
                    onSuccess(it)
                } ?: Resource.Error("No data received from the server!", null)
            } else {
                val errorStr = response.errorBody()?.string()
                if (errorStr != null) {
                    try {
                        Resource.Error(JSONObject(errorStr).getString("error"), null)
                    } catch (e: JSONException) {
                        Resource.Error(response.errorBody()?.string() ?: "Operation failed with unknown error", null)
                    }
                } else {
                    Resource.Error(response.errorBody()?.string() ?: "Operation failed with unknown error")
                }
            }
        }

    override suspend fun registerUser(ud: UserData,d:Disease): Resource<FullAutUserData> {
        return try {
            val regResponse = apiDao.register(RegisterUser(userData = ud, disease = d))
            handleApiResponse(regResponse) { fullAutUserData ->
                // Saving the tokens
                sessionManagement.saveTokens(fullAutUserData.tokens.access, fullAutUserData.tokens.refresh)
                // Caching user data
                sessionManagement.storeUserData(fullAutUserData.userData)
                Resource.Success(fullAutUserData)
            }
        } catch (e: Exception) {
            // Handle network exceptions
            Resource.Error(e.message ?: "Network error!", null)
        }
    }
    override suspend fun loginAttempt(email: String, password: String): Resource<FullAutUserData> {
        return try {
            val loginResponse = apiDao.login(LoginData(email=email,password=password))
            handleApiResponse(loginResponse) { fullAutUserData ->
                // Saving the tokens
                sessionManagement.saveTokens(fullAutUserData.tokens.access, fullAutUserData.tokens.refresh)
                // Caching user data
                sessionManagement.storeUserData(fullAutUserData.userData)
                Resource.Success(fullAutUserData)
            }
        } catch (e: Exception) {
            // Handle network exceptions
            Resource.Error(e.message ?: "Network error!", null)
        }
    }

    override suspend fun updateAccessToken(): Resource<Tokens>{
        return try {
            //Getting the stored token
            val refreshToken = sessionManagement.getRefreshToken()
            if (refreshToken != null) {
                val tokensResponse = apiDao.getAccessToken(Tokens(refresh = refreshToken, access = ""))
                if (tokensResponse.isSuccessful) {
                    val tokens = tokensResponse.body()
                    if (tokens != null) {
                        sessionManagement.saveTokens(tokens.access, tokens.refresh)
                        Resource.Success(tokens)
                    } else {
                        restartMainActivity()
                        Resource.Error("Failed to parse tokens")
                    }
                } else {
                    restartMainActivity()
                    Resource.Error("Failed to fetch new access token: ${tokensResponse.message()}")
                }
            } else {
                restartMainActivity()
                Resource.Error("No refresh token available")
            }
        } catch (e: Exception) {
            sessionManagement.deleteTokens()
            restartMainActivity()
            Resource.Error(e.message ?: "Network error!")
        }
    }

    // Functions from here need Access tokens so they need to be handled by handleApiResponseRestart
    override suspend fun getCalories(): Resource<CaloriesData> {
        return try {
            // Meghívja az API függvényt és kezeli a választ a handleApiResponse segítségével
            val response = apiDao.getCalories(authToken = sessionManagement.getBearerToken())
            handleApiResponse(response) { responseData ->
                Resource.Success(responseData)
            }
        } catch (e: Exception) {
            // Hálózati hibák kezelése
            Resource.Error(e.message ?: "Network error!", null)
        }
    }

    //apiDao.addCalories(caloriesData)
    override suspend fun addCalories(caloriesData: CaloriesData): Resource<String> {
        return try {
            // Meghívja az API függvényt és kezeli a választ a handleApiResponse segítségével
            val response = apiDao.addCalories(caloriesData = caloriesData, authToken = sessionManagement.getBearerToken())
            handleApiResponse(response) { responseData ->
                Resource.Success(responseData)
            }
        } catch (e: Exception) {
            // Hálózati hibák kezelése
            Resource.Error(e.message ?: "Network error!", null)
        }
    }

    override suspend fun getRecomWorkouts(): Resource<List<Workout>> {
        TODO("Not yet implemented")
    }

    override suspend fun postUserWorkout(
        workoutData: SelectedWorkout
    ): Resource<SelectedWorkout> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserWorkout(): Resource<List<UserWorkout>> {
        TODO("Not yet implemented")
    }

    override suspend fun workoutDone(
        workoutDoneData: WorkoutDone
    ): Resource<WorkoutDone> {
        TODO("Not yet implemented")
    }

    override suspend fun getStats(
        year: Int,
        month: Int,
        day: Int
    ): Resource<WeekStats> {
        TODO("Not yet implemented")
    }

    override suspend fun getStatsViaEmail(
        csv: Boolean,
        pdf: Boolean
    ): Resource<Success> {
        TODO("Not yet implemented")
    }


    //This function returns user data from the cache
    override suspend fun getUserData(): Resource<UserData> {
        return try {
            return Resource.Success(sessionManagement.getUserData())
        } catch (e: Exception) {
            // Handle any exceptions that might occur during the network request
            Resource.Error(e.message ?: "Exception occurred while fetching user data", null)
        }
    }
    override suspend fun deleteUserData() {
        TODO("Not yet implemented")
    }

}