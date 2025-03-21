package com.elte_r532ov.musclemind.data.api

//Wrapper for API calls
sealed class Resource<T>(val data: T? = null,val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String,data: T? = null): Resource<T>(data,message)
}