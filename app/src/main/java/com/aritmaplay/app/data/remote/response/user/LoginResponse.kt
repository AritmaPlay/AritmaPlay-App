package com.aritmaplay.app.data.remote.response.user

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val response_code: Int,
    val data: Data
)

data class Data(
    val token: String
)