package com.aritmaplay.app.data.remote.response.user

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val response_code: Int,
    val data: RegisterData
)

data class RegisterData(
    val username: String,
    val email: String,
    val updated_at: String,
    val created_at: String,
    val id: Int
)