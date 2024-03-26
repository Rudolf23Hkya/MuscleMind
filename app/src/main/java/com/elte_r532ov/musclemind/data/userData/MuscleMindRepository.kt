package com.elte_r532ov.musclemind.data.userData

//This is an abstraction for transferring data from and to the app
interface MuscleMindRepository {

    suspend fun insertUserData(ud : UserData)

    suspend fun deleteUserData(ud : UserData)

    suspend fun loginAttempt(email : String, password : String): UserData?

    suspend fun getUserBySessionToken(sT : String): UserData?
}