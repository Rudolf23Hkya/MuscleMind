package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.userData.MuscleMindRepository
import com.elte_r532ov.musclemind.data.userData.UserData

class MuscleMindRepoImplApi(private val apiDao: ApiDao)
    : MuscleMindRepository {
    override suspend fun insertUserData(ud: UserData):Boolean {
        val response = apiDao.register(ud)
        if (response.isSuccessful) {
            return true
        } else {
            return false
        }
    }

    override suspend fun deleteUserData(ud: UserData) {
        TODO("Not yet implemented")
    }

    override suspend fun loginAttempt(email: String, password: String): UserData? {
        val loginResponse = apiDao.login(mapOf("email" to email, "password" to password))
        if (loginResponse.isSuccessful) {
            return loginResponse.body()
        }

        return null
    }

    override suspend fun getUserBySessionToken(sT: String): UserData? {
        val response = apiDao.getUserByAccessToken(sT)
        if (response.isSuccessful) {
            return response.body()
        }
        else{
            //TODO
            //If user data can not be received with access token, then
            //We try to get a new access token with the refresh token
        }
        return null
    }

    override suspend fun modifyUserData(ud: UserData): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun modifyPassword(ud: UserData): Boolean {
        TODO("Not yet implemented")
    }

}