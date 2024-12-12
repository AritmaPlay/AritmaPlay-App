package com.aritmaplay.app.data.remote.response.handwriting.predict

data class HandwritingPredictData(
    val digit: Int,
    val probabilities: List<Double>
)
