package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.api.responses.FullAutUserData
import com.elte_r532ov.musclemind.data.api.responses.ReqDate
import com.elte_r532ov.musclemind.data.api.responses.Tokens
import com.elte_r532ov.musclemind.data.api.responses.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiDao {
    @POST("loginUser/")
    suspend fun login(@Body loginRequest: Map<String, String>): Response<FullAutUserData>

    @POST("regUser/")
    suspend fun register(@Body userData: UserData): Response<FullAutUserData>

    @GET("user_with_access_token/")
    suspend fun getUserWithAccessToken(@Query("access_token") accessToken: String): Response<UserData>

    @POST("access_token_with_refresh_token/")
    suspend fun getAccessTokenWithRefreshToken(@Query("refresh_token") refreshToken: String): Response<Tokens>

    //Using Post
    @POST("get_stats/")
    suspend fun getStats(@Body date: ReqDate):Response<UserData>
}
