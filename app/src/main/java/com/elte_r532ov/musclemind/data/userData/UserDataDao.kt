package com.elte_r532ov.musclemind.data.userData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//Data Access Object
//Abstraction from where UserData can be accessed
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

}