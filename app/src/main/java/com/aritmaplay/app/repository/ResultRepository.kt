package com.aritmaplay.app.repository

import android.util.Log
import com.aritmaplay.app.data.remote.response.result.ResultResponse
import com.aritmaplay.app.data.remote.response.vertexai.generate.VertexAIGenerateMotivationResponse
import com.aritmaplay.app.data.remote.retrofit.profile.ProfileApiService
import com.aritmaplay.app.data.remote.retrofit.vertexai.VertexAIApiService

class ResultRepository private constructor(
    private val profileApiService: ProfileApiService,
    private val vertexAIApiService: VertexAIApiService,
){
    suspend fun result(token: String, quizMode: String, totalQuestion: Int, quizTime: Int, correctQuestion: Int, userId: Int): ResultResponse {
        return profileApiService.result(token, quizMode, totalQuestion, quizTime, correctQuestion, userId)
    }

    suspend fun generateMotivation(name: String, correctQuestion: Int, time: Int, totalQuestion: Int, mode: String): VertexAIGenerateMotivationResponse {
        return vertexAIApiService.generate(name, correctQuestion, time, totalQuestion, mode)
    }

    companion object {
        @Volatile private var instance: ResultRepository? = null

        fun getInstance(apiService: ProfileApiService, vertexAIApiService: VertexAIApiService): ResultRepository {
            Log.d("ResultRepository", "getInstance")
            return instance ?: synchronized(this) {
                instance ?: ResultRepository(apiService, vertexAIApiService).also { instance = it }
            }
        }
    }
}