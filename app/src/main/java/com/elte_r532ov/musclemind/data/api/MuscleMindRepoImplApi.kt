package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.api.responses.UserData
import com.elte_r532ov.musclemind.data.sessionManagement.SessionManagement
import com.elte_r532ov.musclemind.data.MuscleMindRepository

class MuscleMindRepoImplApi(
    private val apiDao: ApiDao,
    private val sessionManagement: SessionManagement
    )
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

    override suspend fun loginAttempt(email: String, password: String): Boolean {
        val loginResponse = apiDao.login(mapOf("email" to email, "password" to password))
        if (loginResponse.isSuccessful) {
            //sessionManagement.saveSessionToken()
            loginResponse.body()
            return true
        }

        return false
    }

    override suspend fun getUserBySessionToken(sT: String): UserData? {
        val response = apiDao.getUserWithAccessToken(sT)
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