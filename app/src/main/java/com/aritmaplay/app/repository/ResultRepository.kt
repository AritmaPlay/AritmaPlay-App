package com.aritmaplay.app.repository

import android.util.Log
import com.aritmaplay.app.data.remote.response.result.ResultResponse
import com.aritmaplay.app.data.remote.retrofit.profile.ProfileApiService

class ResultRepository private constructor(
    private val profileApiService: ProfileApiService
){
    suspend fun result(token: String, quizMode: String, totalQuestion: Int, quizTime: Int, correctQuestion: Int): ResultResponse {
        return profileApiService.result(token, quizMode, totalQuestion, quizTime, correctQuestion)
    }

    companion object {
        @Volatile private var instance: ResultRepository? = null

        fun getInstance(apiService: ProfileApiService): ResultRepository {
            Log.d("ResultRepository", "getInstance")
            return instance ?: synchronized(this) {
                instance ?: ResultRepository(apiService).also { instance = it }
            }
        }
    }
}