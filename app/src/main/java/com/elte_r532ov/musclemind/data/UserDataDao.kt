package com.elte_r532ov.musclemind.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow // - Jól fog jonni majd, de nem ide
//Data Access Object
//Abstraction from where UserData can be accessed
@Dao
interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUserData(ud :UserData)
    @Delete
    suspend fun deleteUserData(ud :UserData)
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserDataById(id : Int): UserData?

}