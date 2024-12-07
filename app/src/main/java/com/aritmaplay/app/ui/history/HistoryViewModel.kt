package com.aritmaplay.app.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.quiz.QuizHistoryResponse
import com.aritmaplay.app.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    private val _historyList = MutableLiveData<Result<QuizHistoryResponse>>()
    val historyList: LiveData<Result<QuizHistoryResponse>> get() = _historyList

    fun getHistory(token: String, id: Int) {
        viewModelScope.launch {
            _historyList.value = Result.Loading
            try {
                val response = repository.getQuizHistory(token, id)
                _historyList.value = Result.Success(response)
            } catch (e: Exception) {
                _historyList.value = Result.Error(e.toString())
            }
        }
    }
}