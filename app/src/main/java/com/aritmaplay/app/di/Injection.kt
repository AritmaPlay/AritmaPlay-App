package com.aritmaplay.app.di

import android.content.Context
import com.aritmaplay.app.data.UserRepository
import com.aritmaplay.app.data.pref.UserPreference
import com.aritmaplay.app.data.pref.dataStore
import com.aritmaplay.app.data.retrofit.UserApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = UserApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}