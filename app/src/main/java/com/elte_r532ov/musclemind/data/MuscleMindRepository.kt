package com.elte_r532ov.musclemind.data

//This is an abstraction for transferring data from and to the app
interface MuscleMindRepository {

    suspend fun insertUserData(ud :UserData)

    suspend fun deleteUserData(ud :UserData)

    suspend fun getUserDataById(id : Int): UserData?
}