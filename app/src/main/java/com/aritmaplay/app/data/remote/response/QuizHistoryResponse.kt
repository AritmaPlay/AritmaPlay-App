package com.aritmaplay.app.data.remote.response.quiz


import com.google.gson.annotations.SerializedName

data class QuizHistoryResponse(
    @field:SerializedName("data")
    val data: List<QuizHistoryItem>,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("response_code")
    val responseCode: Int,
    @field:SerializedName("success")
    val success: Boolean
)

data class QuizHistoryItem(
    @field:SerializedName("correct_question")
    val correctQuestion: Int,
    @field:SerializedName("created_at")
    val createdAt: String,
    @field:SerializedName("exp_received")
    val expReceived: Int,
    @field:SerializedName("quiz_id")
    val quizId: Int,
    @field:SerializedName("quiz_mode")
    val quizMode: String,
    @field:SerializedName("quiz_time")
    val quizTime: Int,
    @field:SerializedName("total_question")
    val totalQuestion: Int,
    @field:SerializedName("updated_at")
    val updatedAt: String,
    @field:SerializedName("user_id")
    val userId: Int
)