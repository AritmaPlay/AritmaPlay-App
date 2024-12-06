package com.aritmaplay.app.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import com.aritmaplay.app.data.local.quiz.QuizModel
import com.aritmaplay.app.data.remote.response.handwriting.predict.HandwritingPredictResponse
import com.aritmaplay.app.data.remote.retrofit.handwriting.HandwritingPredictApiService
import com.aritmaplay.app.data.remote.retrofit.user.UserApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.random.Random

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

    fun generateQuestion(operation: String): QuizModel {
        var num1: Int
        var num2: Int
        var result: Int
        var questionType: Int
        var operator: String

        do {
            operator = when (operation) {
                "Penjumlahan" -> "+"
                "Pengurangan" -> "-"
                "Perkalian" -> "x"
                "Pembagian" -> ":"
                else -> "+"
            }

            if (operator == ":") {
                num2 = Random.nextInt(1, 10)
                num1 = num2 * Random.nextInt(1, 10)
            } else {
                num1 = Random.nextInt(1, 10)
                num2 = Random.nextInt(1, 10)
            }

            result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "x" -> num1 * num2
                ":" -> num1 / num2
                else -> 0
            }
        } while (result !in 0..9)

        questionType = Random.nextInt(0, 3)

        if (operator == ":" && questionType == 1 && num1 !in 0..9) {
            questionType = 0
        }

        val question = when (questionType) {
            0 -> "$num1 $operator $num2 = ..."
            1 -> "... $operator $num2 = $result"
            2 -> "$num1 $operator ... = $result"
            else -> ""
        }

        val correctAnswer = when (questionType) {
            0 -> result
            1 -> num1
            2 -> num2
            else -> 0
        }

        return QuizModel(question, correctAnswer)
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