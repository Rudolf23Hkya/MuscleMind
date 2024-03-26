package com.elte_r532ov.musclemind.data.userData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
    (tableName = "users",
    indices = [Index(value = ["email"], unique = true)])
data class UserData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val sessionToken: String,
    @ColumnInfo(name = "email")val email: String,
    val name: String,
    val password: String,
    val gender: Gender,
    val experienceLevel: ExperienceLevel,
    val age: Int,
    val weight: Double,
    val height: Double
)