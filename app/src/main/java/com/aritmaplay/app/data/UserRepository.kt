package com.aritmaplay.app.data

import com.aritmaplay.app.data.pref.UserModel
import com.aritmaplay.app.data.pref.UserPreference
import com.aritmaplay.app.data.response.LoginResponse
import com.aritmaplay.app.data.response.RegisterResponse
import com.aritmaplay.app.data.retrofit.UserApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: UserApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(username: String, name: String, email: String, password: String) : RegisterResponse {
        return apiService.register(username, name, email, password)
    }

    suspend fun login(email: String, password: String) : LoginResponse {
        val response =  apiService.login(email, password)

        if (!response.success) {
            val user = UserModel(
                token = response.data.token,
                isLogin = true
            )
            saveSession(user)
        }
        return response
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            userApiService: UserApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, userApiService)
            }.also { instance = it }
    }
}