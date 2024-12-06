package com.aritmaplay.app.data.remote.retrofit.profile

import com.aritmaplay.app.data.remote.response.leaderboard.LeaderboardResponse
import com.aritmaplay.app.data.remote.response.user.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProfileApiService {
    @GET("/api/user/{id}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<ProfileResponse>

    @GET("/api/leaderboard-active")
    suspend fun getLeaderboard(
        @Header("Authorization") token: String
    ): LeaderboardResponse
}
