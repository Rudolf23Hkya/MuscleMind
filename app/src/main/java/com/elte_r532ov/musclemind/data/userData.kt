package com.elte_r532ov.musclemind.data

import androidx.room.Entity

@Entity
data class userData(
    val name: String,
    val eMail: String,
    val password: String,
    val age: Int,
    val sex: String,

)
