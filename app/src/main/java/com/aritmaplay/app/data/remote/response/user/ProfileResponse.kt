package com.aritmaplay.app.data.remote.response.user

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("response_code")
    val responseCode: Int? = null,

    @field:SerializedName("data")
    val data: ProfileData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Stats(

    @field:SerializedName("quiz_bagi_success_rate")
    val quizBagiSuccessRate: Int? = null,

    @field:SerializedName("quiz_tambah_success_rate")
    val quizTambahSuccessRate: Int? = null,

    @field:SerializedName("quiz_kurang_success_rate")
    val quizKurangSuccessRate: Int? = null,

    @field:SerializedName("quiz_done")
    val quizDone: Int? = null,

    @field:SerializedName("quiz_kali_success_rate")
    val quizKaliSuccessRate: Int? = null
)

data class ProfileData(

    @field:SerializedName("stats")
    val stats: Stats? = null,

    @field:SerializedName("user")
    val user: ProfileUser? = null
)

data class ProfileUser(

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
    val urlProfile: Any? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("totalExp")
    val totalExp: Int? = null,

    @field:SerializedName("username")
    val username: String? = null
)