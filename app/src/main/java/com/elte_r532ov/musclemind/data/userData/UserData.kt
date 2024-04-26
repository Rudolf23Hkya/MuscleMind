package com.elte_r532ov.musclemind.data.userData


data class UserData(
    val id: Int,
    val email: String,
    val name: String,
    val password: String,
    val gender: Gender,
    val experienceLevel: ExperienceLevel,
    val age: Int,
    val weight: Double,
    val height: Double
)