package com.aritmaplay.app.repository

import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.remote.response.user.ProfileResponse
import com.aritmaplay.app.data.remote.retrofit.profile.ProfileApiService
import retrofit2.Response

class ProfileRepository private constructor(
    val pref: UserPreference,
    private val apiService: ProfileApiService
) {

    suspend fun getProfile(token: String, userId: Int): Response<ProfileResponse> {
        return apiService.getProfile(token, userId)
    }

    companion object {
        fun getInstance(
            pref: UserPreference,
            apiService: ProfileApiService,
        ): ProfileRepository {
            return ProfileRepository(pref, apiService)
        }
    }
}