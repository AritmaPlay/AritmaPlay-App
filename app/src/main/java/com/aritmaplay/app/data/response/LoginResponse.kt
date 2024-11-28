package com.aritmaplay.app.data.response

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val response_code: Int,
    val data: Data
)

data class Data(
    val token: String
)