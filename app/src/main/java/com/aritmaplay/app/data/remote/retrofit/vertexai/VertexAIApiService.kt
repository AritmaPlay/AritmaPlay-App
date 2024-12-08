package com.aritmaplay.app.data.remote.retrofit.vertexai

import com.aritmaplay.app.data.remote.response.vertexai.generate.VertexAIGenerateMotivationResponse
import com.aritmaplay.app.data.remote.response.vertexai.health.VertexAIHealthResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface VertexAIApiService {
    @GET("health")
    suspend fun health(): VertexAIHealthResponse

    @FormUrlEncoded
    @POST("generate-motivation")
    suspend fun generate(
        @Field("name") name: String,
        @Field("correct_answer") correctAnswer: Int,
        @Field("time") time: Int,
        @Field("total_question") totalQuestion: Int,
        @Field("mode") mode: String,
    ): VertexAIGenerateMotivationResponse
}