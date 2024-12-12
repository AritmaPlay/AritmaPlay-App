package com.aritmaplay.app.data.remote.response.vertexai.generate


import com.google.gson.annotations.SerializedName

data class VertexAIGenerateMotivationResponse(
    @field:SerializedName("data")
    var `data`: String? = null,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("response_code")
    val responseCode: Int,
    @field:SerializedName("success")
    val success: Boolean
)