package com.aritmaplay.app.di

import android.content.Context
import com.aritmaplay.app.repository.UserRepository
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.data.remote.retrofit.handwriting.HandwritingPredictApiConfig
import com.aritmaplay.app.data.remote.retrofit.user.UserApiConfig
import com.aritmaplay.app.repository.QuizRepository

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
}