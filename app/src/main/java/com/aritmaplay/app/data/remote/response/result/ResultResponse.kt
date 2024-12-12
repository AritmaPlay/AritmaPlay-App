package com.aritmaplay.app.data.remote.response.result

import com.google.gson.annotations.SerializedName

data class ResultResponse(

    @field:SerializedName("response_code")
    val responseCode: Int? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class DataItem(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("level")
    val level: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("urlProfile")
    val urlProfile: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("totalExp")
    val totalExp: Int? = null,

    @field:SerializedName("username")
    val username: String? = null
)

data class Data(

    @field:SerializedName("quiz")
    val quiz: List<QuizItem?>? = null,

    @field:SerializedName("user_update")
    val userUpdate: List<UserUpdateItem?>? = null
)

data class UserUpdateItem(

    @field:SerializedName("response_code")
    val responseCode: Int? = null,

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class QuizItem(

    @field:SerializedName("quiz_id")
    val quizId: Int? = null,

    @field:SerializedName("quiz_mode")
    val quizMode: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("correct_question")
    val correctQuestion: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("total_question")
    val totalQuestion: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("quiz_time")
    val quizTime: String? = null,

    @field:SerializedName("exp_received")
    val expReceived: Int? = null
)