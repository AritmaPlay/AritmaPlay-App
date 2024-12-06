package com.aritmaplay.app.data.remote.response.leaderboard

import com.google.gson.annotations.SerializedName

data class LeaderboardResponse(

    @field:SerializedName("response_code")
    val responseCode: Int? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class LeaderboardEntriesItem(

    @field:SerializedName("leaderboard_id")
    val leaderboardId: Int? = null,

    @field:SerializedName("last_updated")
    val lastUpdated: Any? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("totalExpPerWeek")
    val totalExpPerWeek: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("entry_id")
    val entryId: Int? = null,

    @field:SerializedName("user")
    val user: User? = null,

    @field:SerializedName("rank")
    val rank: Int = 0
)

data class User(

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

data class Data(

    @field:SerializedName("leaderboard_id")
    val leaderboardId: Int? = null,

    @field:SerializedName("end_date")
    val endDate: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("leaderboard_entries")
    val leaderboardEntries: List<LeaderboardEntriesItem?>? = null,

    @field:SerializedName("userRank")
    val userRank: Int? = null,

    @field:SerializedName("start_date")
    val startDate: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)