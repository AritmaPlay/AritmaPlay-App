package com.aritmaplay.app.data.retrofit

import com.aritmaplay.app.data.response.LoginResponse
import com.aritmaplay.app.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApiService {
    @FormUrlEncoded
    @POST("api/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}