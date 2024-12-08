package com.aritmaplay.app.data.remote.retrofit.profile

import com.aritmaplay.app.data.remote.response.leaderboard.AllTimeLeaderboardResponse
import com.aritmaplay.app.data.remote.response.leaderboard.WeeklyLeaderboardResponse
import com.aritmaplay.app.data.remote.response.quiz.QuizHistoryResponse
import com.aritmaplay.app.data.remote.response.result.ResultResponse
import com.aritmaplay.app.data.remote.response.user.ProfileResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileApiService {
    @GET("/api/user/{id}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<ProfileResponse>

    @GET("/api/leaderboard-active")
    suspend fun getWeeklyLeaderboard(
        @Header("Authorization") token: String
    ): WeeklyLeaderboardResponse

    @GET("/api/leaderboard-total-exp")
    suspend fun getAllTimeLeaderboard(
        @Header("Authorization") token: String
    ): AllTimeLeaderboardResponse

    @GET("/api/quiz/user/{id}")
    suspend fun getQuizHistory(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): QuizHistoryResponse

    @FormUrlEncoded
    @POST("/api/quiz")
    suspend fun result(
        @Header("Authorization") token: String,
        @Field("quiz_mode") quizMode: String,
        @Field("total_question") totalQuestion: Int,
        @Field("quiz_time") quizTime: Int,
        @Field("correct_question") correctQuestion: Int,
    ): ResultResponse
}