package com.aritmaplay.app.di

import android.content.Context
import com.aritmaplay.app.data.UserRepository
import com.aritmaplay.app.data.pref.UserPreference
import com.aritmaplay.app.data.pref.dataStore


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}