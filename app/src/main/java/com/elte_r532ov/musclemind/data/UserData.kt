package com.elte_r532ov.musclemind.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.TypeConverter
import androidx.room.PrimaryKey

@Entity
    (tableName = "users",
    indices = [Index(value = ["email"], unique = true)])
data class UserData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    @ColumnInfo(name = "email")val email: String,
    val password: String,
    val age: Int,
    val gender: Gender,
    val experienceLevel: ExperienceLevel,
    val weight: Double
)

class Converters {
    @TypeConverter
    fun fromSex(gender: Gender): String {
        return gender.name
    }
    @TypeConverter
    fun toSex(gender: String): Gender {
        return Gender.valueOf(gender)
    }
    @TypeConverter
    fun fromExperienceLevel(exp: ExperienceLevel): String {
        return exp.name
    }
    @TypeConverter
    fun toExperienceLevel(exp: String): ExperienceLevel {
        return ExperienceLevel.valueOf(exp)
    }
}
