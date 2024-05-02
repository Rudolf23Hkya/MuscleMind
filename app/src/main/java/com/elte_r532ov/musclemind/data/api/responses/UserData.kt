package com.elte_r532ov.musclemind.data.api.responses

import com.elte_r532ov.musclemind.data.enums.ExperienceLevel
import com.elte_r532ov.musclemind.data.enums.Gender

data class UserData(
    val email: String,
    val username: String,
    val password: String = "",
    val age: Int,
    val experiencelevel: ExperienceLevel,
    val gender: Gender,
    val height: Double,
    val weight: Double
)