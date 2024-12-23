package com.aritmaplay.app.ui.quiz

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.local.quiz.QuizModel
import com.aritmaplay.app.data.remote.response.error.ErrorResponse
import com.aritmaplay.app.data.remote.response.handwriting.predict.HandwritingPredictResponse
import com.aritmaplay.app.repository.QuizRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class QuizViewModel(private val quizRepository: QuizRepository) : ViewModel() {
    private val _predictResult = MutableLiveData<Result<HandwritingPredictResponse>>()
    val predictResult: LiveData<Result<HandwritingPredictResponse>> = _predictResult

    private val _currentQuestion = MutableLiveData<QuizModel>()
    val currentQuestion: LiveData<QuizModel> get() = _currentQuestion

    private val _correctAnswer = MutableLiveData<Int>()

    fun generateNewQuestion(operation: String) {
        _currentQuestion.value = quizRepository.generateQuestion(operation)
    }

    fun getDuration(startTime: Long, endTime: Long): Int {
        return quizRepository.getDuration(startTime, endTime)
    }

    fun incrementCorrectAnswer(): Int {
        return quizRepository.incrementCorrectAnswer()
    }

    fun getCorrectAnswer(): Int {
        return quizRepository.getCorrectAnswer()
    }

    fun resetCorrectAnswer() {
        quizRepository.resetCorrectAnswer()
        _correctAnswer.value = 0
    }

    fun predict(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            _predictResult.value = Result.Loading
            try {
                val image = quizRepository.convertBitmapToMultipart(bitmap, context, 48)
                val response = quizRepository.predict(image)
                _predictResult.value = Result.Success(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _predictResult.value = errorMessage?.let { Result.Error(it) }
            } catch (e: IOException) {
                _predictResult.value = Result.Error("Network error occurred. Please try again.")
            } catch (e: Exception) {
                _predictResult.value = Result.Error("Unexpected error occurred: ${e.message}")
            }
        }
    }
}