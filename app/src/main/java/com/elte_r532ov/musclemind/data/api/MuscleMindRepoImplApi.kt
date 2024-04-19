package com.elte_r532ov.musclemind.data.api
import com.elte_r532ov.musclemind.data.userData.MuscleMindRepository
import com.elte_r532ov.musclemind.data.userData.UserData

class MuscleMindRepoImplApi(private val dao: ApiDao)
    : MuscleMindRepository {
    override suspend fun insertUserData(ud: UserData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserData(ud: UserData) {
        TODO("Not yet implemented")
    }

    override suspend fun loginAttempt(email: String, password: String): UserData? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserBySessionToken(sT: String): UserData? {
        TODO("Not yet implemented")
    }

    override suspend fun modifyUserData(ud: UserData): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun modifyPassword(ud: UserData): Boolean {
        TODO("Not yet implemented")
    }

}