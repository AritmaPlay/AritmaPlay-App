package com.aritmaplay.app.repository

import com.aritmaplay.app.data.local.pref.UserModel
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.remote.response.user.LoginResponse
import com.aritmaplay.app.data.remote.response.user.RegisterResponse
import com.aritmaplay.app.data.remote.retrofit.user.UserApiService
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

    suspend fun register(username: String, name: String, email: String, password: String, urlProfile: String): RegisterResponse {
        return apiService.register(username, name, email, password, urlProfile)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)

        if (response.success == true) {
            response.data?.token?.let { token ->
                response.data.user?.userId?.let { userId ->
                    val user = UserModel(
                        token = token,
                        isLogin = true,
                        userId =  userId
                    )
                    saveSession(user)
                } ?: throw IllegalStateException("Token is null in LoginResponse")
            } ?: throw IllegalStateException("Token is null in LoginResponse")
        } else {
            throw Exception(response.message ?: "Login failed")
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