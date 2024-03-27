package com.elte_r532ov.musclemind.data.userData

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elte_r532ov.musclemind.data.RoomDBConverters

@Database(
    entities = [UserData :: class],
    version = 4
)
@TypeConverters(RoomDBConverters::class)
abstract class MuscleMindDB : RoomDatabase(){
    abstract  val dao: UserDataDao
}