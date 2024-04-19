package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.userData.UserData
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiDao {

    //Returns a session Token
    @POST("regUser")
    fun regAttempt(email : String,password: String): String

    //Returns a session Token
    @GET("loginUser")
    fun loginAttempt(email : String,password: String): String
}
/*
@Dao
interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUserData(ud : UserData)
    @Delete
    suspend fun deleteUserData(ud : UserData)
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun loginAttempt(email : String,password: String): UserData?

    @Query("SELECT * FROM users WHERE sessionToken = :sT")
    suspend fun getUserBySessionToken(sT : String): UserData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun modifyData(ud: UserData)

}
*/
