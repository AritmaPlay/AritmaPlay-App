package com.aritmaplay.app.repository

import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.remote.response.leaderboard.LeaderboardResponse
import com.aritmaplay.app.data.remote.retrofit.profile.ProfileApiService

class LeaderboardRepository private constructor(
    private val pref: UserPreference,
    private val apiService: ProfileApiService
) {
    suspend fun getLeaderboard(token: String): LeaderboardResponse {
        return apiService.getLeaderboard(token)
    }

    companion object {
        fun getInstance(
            pref: UserPreference,
            apiService: ProfileApiService,
        ): LeaderboardRepository {
            return LeaderboardRepository(pref, apiService)
        }
    }
}