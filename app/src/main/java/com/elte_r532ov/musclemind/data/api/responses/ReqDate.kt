package com.elte_r532ov.musclemind.data.api.responses

data class ReqDate(
    val accessToken: String = "",
    val day: Int,
    val month: Int,
    val year: Int
)