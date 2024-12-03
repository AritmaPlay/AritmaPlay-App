package com.aritmaplay.app.data.remote.retrofit.handwriting

import com.aritmaplay.app.data.remote.response.handwriting.health.HandwritingHealthResponse
import com.aritmaplay.app.data.remote.response.handwriting.predict.HandwritingPredictResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface HandwritingPredictApiService {
    @GET("health")
    suspend fun health(): HandwritingHealthResponse

    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part image: MultipartBody.Part,
    ): HandwritingPredictResponse
}