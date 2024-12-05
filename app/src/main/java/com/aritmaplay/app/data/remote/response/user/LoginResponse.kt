package com.aritmaplay.app.data.remote.response.user

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("response_code")
    val responseCode: Int? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class User(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("level")
    val level: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("urlProfile")
    val urlProfile: Any? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("totalExp")
    val totalExp: Int? = null,

    @field:SerializedName("username")
    val username: String? = null
)

data class Data(
    @field:SerializedName("user")
    val user: User? = null,

    @field:SerializedName("token")
    val token: String? = null
)