package com.elte_r532ov.musclemind.data

//This is the implementation for transferring data from and to the app
class MuscleMindRepoImpl(
    private val dao : UserDataDao
) : MuscleMindRepository{
    override suspend fun insertUserData(ud: UserData) {
        dao.insertUserData(ud)
    }

    override suspend fun deleteUserData(ud: UserData) {
        dao.deleteUserData(ud)
    }

    override suspend fun getUserDataById(id: Int): UserData? {
        return dao.getUserDataById(id)
    }
}