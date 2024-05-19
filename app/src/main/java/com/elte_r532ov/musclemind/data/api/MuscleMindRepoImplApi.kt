package com.elte_r532ov.musclemind.data.api
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
    private val sessionManagement: SessionManagement
    )
    : MuscleMindRepository {
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

    override suspend fun getAccessToken(): Resource<Tokens> {
        TODO("Not yet implemented")
    }

    override suspend fun getCalories(): Resource<CaloriesData> {
        TODO("Not yet implemented")
    }

    override suspend fun addCalories(
        caloriesData: CaloriesData
    ): Resource<CaloriesData> {
        TODO("Not yet implemented")
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