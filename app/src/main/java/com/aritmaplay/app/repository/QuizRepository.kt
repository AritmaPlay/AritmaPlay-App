package com.aritmaplay.app.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import com.aritmaplay.app.data.remote.response.handwriting.predict.HandwritingPredictResponse
import com.aritmaplay.app.data.remote.retrofit.handwriting.HandwritingPredictApiService
import com.aritmaplay.app.data.remote.retrofit.user.UserApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class QuizRepository private constructor(
    private val userApiService: UserApiService,
    private val handwritingPredictApiService: HandwritingPredictApiService
){

    fun convertBitmapToMultipart(bitmap: Bitmap, context: Context, size: Int = 48): MultipartBody.Part {
        val scaleWidth = size.toFloat() / bitmap.width
        val scaleHeight = size.toFloat() / bitmap.height

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        val scaledBitmap: Bitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )

        val file = File(context.cacheDir, "upload_image.jpg")

        file.outputStream().use { outputStream ->
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.flush()
        }

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    suspend fun predict(image: MultipartBody.Part): HandwritingPredictResponse {
        val response = handwritingPredictApiService.predict(image)
        return response
    }

    companion object {
        @Volatile
        private var instance: QuizRepository? = null
        fun getInstance(
            userApiService: UserApiService,
            handwritingPredictApiService: HandwritingPredictApiService
        ): QuizRepository =
            instance ?: synchronized(this) {
                instance ?: QuizRepository(userApiService, handwritingPredictApiService)
            }.also { instance = it }
    }
}
