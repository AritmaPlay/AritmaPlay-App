package com.aritmaplay.app.repository

import android.util.Log
import com.aritmaplay.app.data.remote.response.quiz.QuizHistoryResponse
import com.aritmaplay.app.data.remote.retrofit.profile.ProfileApiService

class HistoryRepository private constructor(
    private val profileApiService: ProfileApiService
){
    suspend fun getQuizHistory(token: String, userId: Int): QuizHistoryResponse {
        return profileApiService.getQuizHistory(token, userId)
    }

    companion object {
        @Volatile private var instance: HistoryRepository? = null

        fun getInstance(profileApiService: ProfileApiService): HistoryRepository {
            Log.d("HistoryRepository", "getInstance")
            return instance ?: synchronized(this) {
                instance ?: HistoryRepository(profileApiService).also { instance = it }
            }
        }
    }
}