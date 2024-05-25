package com.elte_r532ov.musclemind.data.api.responses

data class ExerciseDone(
    val name : String,
    val skipped: Boolean,
    val duration: Int,
    val rating: Int,
    val cal: Int
)
