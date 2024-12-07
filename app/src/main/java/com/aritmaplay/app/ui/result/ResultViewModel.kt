package com.aritmaplay.app.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.result.ResultResponse
import com.aritmaplay.app.repository.ResultRepository
import kotlinx.coroutines.launch

class ResultViewModel(private val resultRepository: ResultRepository) : ViewModel() {

    private val _quizResult = MutableLiveData<Result<ResultResponse>>()
    val quizResult: LiveData<Result<ResultResponse>> = _quizResult

    fun result(token: String, quizMode: String, totalQuestion: Int, quizTime: Int, correctQuestion: Int) {
        viewModelScope.launch {
            _quizResult.value = Result.Loading
            try {
                val response = resultRepository.result(token, quizMode, totalQuestion, quizTime, correctQuestion)
                _quizResult.value = Result.Success(response)
            } catch (e: Exception) {
                _quizResult.value = Result.Error(e.toString())
            }
        }
    }
}