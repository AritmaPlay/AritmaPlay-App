package com.aritmaplay.app.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.result.ResultResponse
import com.aritmaplay.app.data.remote.response.vertexai.generate.VertexAIGenerateMotivationResponse
import com.aritmaplay.app.repository.ResultRepository
import kotlinx.coroutines.launch

class ResultViewModel(private val resultRepository: ResultRepository) : ViewModel() {

    private val _quizResult = MutableLiveData<Result<ResultResponse>>()
    val quizResult: LiveData<Result<ResultResponse>> = _quizResult

    fun result(token: String, quizMode: String, totalQuestion: Int, quizTime: Int, correctQuestion: Int, userId: Int) {
        viewModelScope.launch {
            _quizResult.value = Result.Loading
            try {
                val response = resultRepository.result(token, quizMode, totalQuestion, quizTime, correctQuestion, userId)
                _quizResult.value = Result.Success(response)
            } catch (e: Exception) {
                _quizResult.value = Result.Error(e.toString())
            }
        }
    }

    private val _motivation = MutableLiveData<Result<VertexAIGenerateMotivationResponse>>()
    val motivation: LiveData<Result<VertexAIGenerateMotivationResponse>> = _motivation

    fun generateMotivation(name: String, correctQuestion: Int, time: Int, totalQuestion: Int, mode: String) {
        viewModelScope.launch {
            _motivation.value = Result.Loading
            try {
                val response = resultRepository.generateMotivation(name, correctQuestion, time, totalQuestion, mode)
                _motivation.value = Result.Success(response)
            } catch (e: Exception) {
                _motivation.value = Result.Error(e.toString())
            }
        }
    }
}