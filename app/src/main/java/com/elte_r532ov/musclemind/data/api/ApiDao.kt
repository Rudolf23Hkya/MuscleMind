package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.userData.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiDao {
    @POST("loginUser/")
    suspend fun login(@Body loginRequest: Map<String, String>): Response<UserData>

    @POST("regUser/")
    suspend fun register(@Body newUser: UserData): Response<UserData>

    @GET("user_by_access_token/")
    suspend fun getUserByAccessToken(@Query("session_token") sessionToken: String): Response<UserData>
}
