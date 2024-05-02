package com.elte_r532ov.musclemind.data

import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.util.Resource

//This is an abstraction for transferring data from and to the app
interface MuscleMindRepository {

    suspend fun insertUserData(ud : UserData):Resource<UserData>

    suspend fun deleteUserData(ud : UserData)

    suspend fun loginAttempt(email : String, password : String): Resource<UserData>

    suspend fun getUserData(): Resource<UserData>

    suspend fun modifyUserData(ud : UserData) : Boolean

    suspend fun modifyPassword(ud : UserData) : Boolean
}