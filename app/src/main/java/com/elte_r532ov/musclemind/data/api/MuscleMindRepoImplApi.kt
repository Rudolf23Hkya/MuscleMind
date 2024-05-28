package com.elte_r532ov.musclemind.data.api
import android.content.Context
import android.content.Intent
import com.elte_r532ov.musclemind.MainActivity
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.data.local.SessionManagement
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.CaloriesData
import com.elte_r532ov.musclemind.data.api.responses.Disease
import com.elte_r532ov.musclemind.data.api.responses.FullAutUserData
import com.elte_r532ov.musclemind.data.api.responses.LoginData
import com.elte_r532ov.musclemind.data.api.responses.RegisterUser
import com.elte_r532ov.musclemind.data.api.responses.SelectedWorkout
import com.elte_r532ov.musclemind.data.api.responses.StatsWeek
import com.elte_r532ov.musclemind.data.api.responses.Tokens
import com.elte_r532ov.musclemind.data.api.responses.UserWorkout
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.data.api.responses.WorkoutDone
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

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
    private fun <T> handleApiResponse(
        response: Response<T>,
        onSuccess: (T) -> Resource<T>,
        onError: (String) -> Resource<T>
    ): Resource<T> {
        return if (response.isSuccessful) {
            response.body()?.let {
                onSuccess(it)
            } ?: Resource.Error("No data received from the server!", null)
        } else {
            val errorStr = response.errorBody()?.string()
            if (errorStr != null) {
                try {
                    onError(JSONObject(errorStr).getString("error"))
                } catch (e: JSONException) {
                    onError(response.errorBody()?.string() ?: "Operation failed with unknown error")
                }
            } else {
                onError(response.errorBody()?.string() ?: "Operation failed with unknown error")
            }
        }
    }

    override suspend fun registerUser(ud: UserData, d: Disease): Resource<FullAutUserData> {
        return try {
            val regResponse = apiDao.register(RegisterUser(userData = ud, disease = d))
            handleApiResponse(regResponse, { fullAutUserData ->
                // Saving the tokens
                sessionManagement.saveTokens(fullAutUserData.tokens.access, fullAutUserData.tokens.refresh)
                // Caching user data
                sessionManagement.storeUserData(fullAutUserData.userData)
                Resource.Success(fullAutUserData)
            }, { errorMessage ->
                Resource.Error(errorMessage, null)
            })
        } catch (e: IOException) {
            // Handle network exceptions
            Resource.Error("Network connection issue. Please check your internet connection!", null)
        } catch (e: Exception) {
            // Handle other exceptions
            Resource.Error(e.message ?: "Unknown error!", null)
        }
    }


    override suspend fun loginAttempt(email: String, password: String): Resource<FullAutUserData> {
        return try {
            val loginResponse = apiDao.login(LoginData(email = email, password = password))
            handleApiResponse(loginResponse, { fullAutUserData ->
                // Saving the tokens
                sessionManagement.saveTokens(fullAutUserData.tokens.access, fullAutUserData.tokens.refresh)
                // Caching user data
                sessionManagement.storeUserData(fullAutUserData.userData)
                Resource.Success(fullAutUserData)
            }, { errorMessage ->
                Resource.Error(errorMessage, null)
            })
        } catch (e: IOException) {
            // Handle network exceptions
            Resource.Error("Network connection issue. Please check your internet connection!", null)
        } catch (e: Exception) {
            // Handle other exceptions
            Resource.Error(e.message ?: "Unknown error!", null)
        }
    }

    override suspend fun googleTokenAuth(oAuthToken : String): Resource<FullAutUserData>{
        return try {
            val loginResponse = apiDao.googleTokenAuth(oAuthToken)
            handleApiResponse(loginResponse, { fullAutUserData ->
                if(fullAutUserData.userData.username != "EMAIL_NOT_FOUND"){
                    // If the Google account s email matches a registered e-mail the login is successful
                    sessionManagement.saveTokens(fullAutUserData.tokens.access, fullAutUserData.tokens.refresh)
                    sessionManagement.storeUserData(fullAutUserData.userData)
                    Resource.Success(fullAutUserData)
                }
                else{
                    // If the username is "USER NOT FOUND", the user has no account registered
                    // with the user's Google e-mail.
                    // The user needs to create an account with the e-mail address.
                    Resource.Success(fullAutUserData)
                }
            }, { errorMessage ->
                Resource.Error(errorMessage, null)
            })
        } catch (e: IOException) {
            // Handle network exceptions
            Resource.Error("Network connection issue. Please check your internet connection!", null)
        } catch (e: Exception) {
            // Handle other exceptions
            Resource.Error(e.message ?: "Unknown error!", null)
        }
    }



    override suspend fun updateAccessToken(): Resource<Tokens> {
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
                    Resource.Error("Failed to fetch new access token!")
                }
            } else {
                restartMainActivity()
                Resource.Error("No refresh token available")
            }
        } catch (e: Exception) {
            restartMainActivity()
            Resource.Error(e.message ?: "Network error!")
        }
    }

    // Functions from here need Access tokens so they need to be handled by handleApiResponseRestart
    override suspend fun getCalories(): Resource<CaloriesData> {
        return try {
            val response = apiDao.getCalories(authToken = sessionManagement.getBearerToken())
            handleApiResponse(response, { responseData ->
                Resource.Success(responseData)
            }) { errorMessage ->
                // If access token is invalid, try to update it and retry the request
                val tokenUpdateResult = runBlocking { updateAccessToken() }
                if (tokenUpdateResult is Resource.Success) {
                    val retryResponse = runBlocking {apiDao.getCalories(authToken = sessionManagement.getBearerToken())}
                    handleApiResponse(retryResponse, { retryData ->
                        Resource.Success(retryData)
                    }) {
                        Resource.Error("Server error!", null)
                    }
                } else {
                    Resource.Error(errorMessage, null)
                }
            }
        } catch (e: Exception) {
            restartMainActivity()
            Resource.Error(e.message ?: "Network error!", null)
        }
    }

    override suspend fun addCalories(caloriesData: CaloriesData): Resource<String> {
        return try {
            val response = apiDao.addCalories(authToken = sessionManagement.getBearerToken(),caloriesData = caloriesData)
            handleApiResponse(response, { responseData ->
                Resource.Success(responseData)
            }) {
                // If access token is invalid, try to update it and retry the request
                val tokenUpdateResult = runBlocking { updateAccessToken() }
                if (tokenUpdateResult is Resource.Success) {
                    val retryResponse = runBlocking {apiDao.addCalories(authToken = sessionManagement.getBearerToken(),caloriesData = caloriesData)}
                    handleApiResponse(retryResponse, { retryData ->
                        Resource.Success(retryData)
                    }) {
                        Resource.Error("Server error!", null)
                    }
                } else {
                    Resource.Error("Bad request, try again later!", null)
                }
            }
        } catch (e: Exception) {
            restartMainActivity()
            Resource.Error(e.message ?: "Network error!", null)
        }
    }

    override suspend fun getRecomWorkouts(weightlifting: Boolean, trx: Boolean): Resource<List<Workout>> {
        return try {
            val response = apiDao.getRecomWorkouts(sessionManagement.getBearerToken(), weightlifting, trx)
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: emptyList())
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            restartMainActivity()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun postUserWorkout(workoutData: SelectedWorkout): Resource<SelectedWorkout> {
        return try {
            val response = apiDao.postUserWorkout(sessionManagement.getBearerToken(), workoutData)
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: workoutData)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            restartMainActivity()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
    override suspend fun getUserWorkout(): Resource<List<UserWorkout>> {
        return try {
            val response = apiDao.getUserWorkout(sessionManagement.getBearerToken())
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: emptyList())
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            restartMainActivity()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun workoutDone(workoutDoneData: WorkoutDone): Resource<WorkoutDone> {
        return try {
            val response = apiDao.workoutDone(sessionManagement.getBearerToken(), workoutDoneData)
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: workoutDoneData)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            restartMainActivity()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getStats(
        year: Int,
        month: Int,
        day: Int
    ): Resource<StatsWeek> {
        return try {
            val response = apiDao.getStats(sessionManagement.getBearerToken(), year, month, day)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Empty response body")
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            //restartMainActivity()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getStatsViaEmail(
        csv: Boolean,
        pdf: Boolean
    ): Resource<String> {
        return try {
            val response = apiDao.getStatsViaEmail(sessionManagement.getBearerToken(), csv, pdf)
            if (response.isSuccessful) {
                Resource.Success("Email sent successfully!")
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            restartMainActivity()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }


    //This function returns user data from the cache
    override suspend fun getUserData(): Resource<UserData> {
        return try {
            return Resource.Success(sessionManagement.getUserData())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Exception occurred while fetching user data", null)
        }
    }

}