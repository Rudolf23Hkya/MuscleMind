package com.elte_r532ov.musclemind.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserData :: class],
    version = 3
)
abstract class MuscleMindDB : RoomDatabase(){
    abstract  val dao: UserDataDao
}