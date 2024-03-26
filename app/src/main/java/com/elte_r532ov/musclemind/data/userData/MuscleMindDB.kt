package com.elte_r532ov.musclemind.data.userData

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elte_r532ov.musclemind.data.EnumConverters

@Database(
    entities = [UserData :: class],
    version = 4
)
@TypeConverters(EnumConverters::class)
abstract class MuscleMindDB : RoomDatabase(){
    abstract  val dao: UserDataDao
}