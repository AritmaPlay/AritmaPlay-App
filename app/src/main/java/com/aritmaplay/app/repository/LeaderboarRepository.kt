package com.aritmaplay.app.repository

import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.remote.response.leaderboard.AllTimeLeaderboardResponse
import com.aritmaplay.app.data.remote.response.leaderboard.WeeklyLeaderboardResponse
import com.aritmaplay.app.data.remote.retrofit.profile.ProfileApiService

class LeaderboardRepository private constructor(
    private val pref: UserPreference,
    private val apiService: ProfileApiService
) {
    suspend fun getWeeklyLeaderboard(token: String): WeeklyLeaderboardResponse {
        return apiService.getWeeklyLeaderboard(token)
    }

    suspend fun getAllTimeLeaderboard(token: String): AllTimeLeaderboardResponse {
        return apiService.getAllTimeLeaderboard(token)
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