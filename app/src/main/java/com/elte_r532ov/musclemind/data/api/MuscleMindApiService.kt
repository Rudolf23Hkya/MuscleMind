package com.elte_r532ov.musclemind.data.api

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MuscleMindApiService {

    private const val BASE_URL =
        "http://127.0.0.1:8000/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}