package com.elte_r532ov.musclemind.data

import com.elte_r532ov.musclemind.data.api.responses.UserData

//This is an abstraction for transferring data from and to the app
interface MuscleMindRepository {

    suspend fun insertUserData(ud : UserData):Boolean

    suspend fun deleteUserData(ud : UserData)

    suspend fun loginAttempt(email : String, password : String): Boolean

    suspend fun getUserBySessionToken(sT : String): UserData?

    suspend fun modifyUserData(ud : UserData) : Boolean

    suspend fun modifyPassword(ud : UserData) : Boolean
}