package com.aritmaplay.app.data.remote.response.handwriting.predict

data class HandwritingPredictResponse(
    val message: String,
    val response_code: Int,
    val success: Boolean,
    val data: HandwritingPredictData
)