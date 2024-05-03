package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.ReqDate
import com.elte_r532ov.musclemind.util.Resource
import org.json.JSONException
import org.json.JSONObject

class MuscleMindRepoImplApi(
    private val apiDao: ApiDao,
    private val sessionManagement: SessionManagement
    )
    : MuscleMindRepository {

    override suspend fun insertUserData(ud: UserData): Resource<UserData> {
        return try {
            val regResponse = apiDao.register(ud)
            if (regResponse.isSuccessful) {
                // Checking if the body is not null, then return it wrapped in Resource.Success
                regResponse.body()?.let {
                    //Saving the tokens
                    sessionManagement.saveTokens(it.tokens.access,it.tokens.refresh)
                    //Caching user data
                    sessionManagement.storeUserData(it.userData)

                    Resource.Success(it.userData)
                } ?: Resource.Error("Received null data from API", null)
            } else {
                // The API responded but indicated a failure
                val errorStr = regResponse.errorBody()?.string()
                if(errorStr != null){
                    try {
                        Resource.Error(JSONObject(errorStr).getString("error"), null)
                    }
                    catch (e: JSONException) {
                        Resource.Error(regResponse.errorBody()?.string() ?: "Login failed with unknown error", null)
                    }

                }
                else{
                    Resource.Error(regResponse.errorBody()?.string() ?: "Login failed with unknown error", null)
                }
            }
        } catch (e: Exception) {
            // Handle network exceptions
            Resource.Error(e.message ?: "Network error!", null)
        }
    }

    override suspend fun loginAttempt(email: String, password: String): Resource<UserData> {
        return try {
            val loginResponse = apiDao.login(mapOf("email" to email, "password" to password))
            if (loginResponse.isSuccessful) {
                loginResponse.body()?.let {
                    //Saving the tokens
                    sessionManagement.saveTokens(it.tokens.access,it.tokens.refresh)
                    //Caching user data
                    sessionManagement.storeUserData(it.userData)

                    Resource.Success(it.userData)
                } ?: Resource.Error("Login successful but no user data returned", null)
            } else {
                // The API responded but indicated a failure
                val errorStr = loginResponse.errorBody()?.string()
                if(errorStr != null){
                    try {
                        Resource.Error(JSONObject(errorStr).getString("error"), null)
                        }
                    catch (e: JSONException) {
                        Resource.Error(loginResponse.errorBody()?.string() ?: "Login failed with unknown error", null)
                    }

                }
                else{
                    Resource.Error(loginResponse.errorBody()?.string() ?: "Login failed with unknown error", null)
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions that occur during the network request
            Resource.Error(e.message ?: "Error occurred during login attempt", null)
        }
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

    override suspend fun getStats(rd: ReqDate) {
        TODO("Not yet implemented")
    }

    override suspend fun addCalForDay(kcal :Int): Resource<IntArray> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyUserData(ud: UserData): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun modifyPassword(ud: UserData): Boolean {
        TODO("Not yet implemented")
    }


    override suspend fun deleteUserData(ud: UserData) {
        TODO("Not yet implemented")
    }


}