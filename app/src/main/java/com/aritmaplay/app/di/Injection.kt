package com.aritmaplay.app.di

import android.content.Context
import com.aritmaplay.app.repository.UserRepository
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.data.remote.retrofit.handwriting.HandwritingPredictApiConfig
import com.aritmaplay.app.data.remote.retrofit.profile.ProfileApiConfig
import com.aritmaplay.app.data.remote.retrofit.user.UserApiConfig
import com.aritmaplay.app.repository.HistoryRepository
import com.aritmaplay.app.repository.LeaderboardRepository
import com.aritmaplay.app.repository.ProfileRepository
import com.aritmaplay.app.repository.QuizRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = UserApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideQuizRepository(): QuizRepository {
        val handwritingPredictApiService = HandwritingPredictApiConfig.getHandwritingPredictApiService()
        val userApiService = UserApiConfig.getApiService()
        return QuizRepository.getInstance(userApiService, handwritingPredictApiService)
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ProfileApiConfig.getApiService(user.token)
        return ProfileRepository.getInstance(pref, apiService)
    }

    fun provideLeaderboardRepository(context: Context): LeaderboardRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ProfileApiConfig.getApiService(user.token)
        return LeaderboardRepository.getInstance(pref, apiService)
    }

    fun provideQuizHistoryRepository(context: Context): HistoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ProfileApiConfig.getApiService(user.token)
        return HistoryRepository.getInstance(apiService)
    }
}