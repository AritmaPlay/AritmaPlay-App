package com.aritmaplay.app.ui.quiz

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.handwriting.predict.HandwritingPredictResponse
import com.aritmaplay.app.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(private val quizRepository: QuizRepository) : ViewModel() {
    private val _predictResult = MutableLiveData<Result<HandwritingPredictResponse>>()
    val predictResult: LiveData<Result<HandwritingPredictResponse>> = _predictResult

    fun predict(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            _predictResult.value = Result.Loading
            try {
                val image = quizRepository.convertBitmapToMultipart(bitmap, context, 48)
                val response = quizRepository.predict(image)
                _predictResult.value = Result.Success(response)
            } catch (e: Exception) {
                _predictResult.value = Result.Error(e.toString())
            }
        }
    }
}