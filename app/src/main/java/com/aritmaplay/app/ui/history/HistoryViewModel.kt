package com.aritmaplay.app.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.error.ErrorResponse
import com.aritmaplay.app.data.remote.response.quiz.QuizHistoryResponse
import com.aritmaplay.app.repository.HistoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    private val _historyList = MutableLiveData<Result<QuizHistoryResponse>>()
    val historyList: LiveData<Result<QuizHistoryResponse>> get() = _historyList

    fun getHistory(token: String, id: Int) {
        viewModelScope.launch {
            _historyList.value = Result.Loading
            try {
                val response = repository.getQuizHistory(token, id)
                _historyList.value = Result.Success(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _historyList.value = errorMessage?.let { Result.Error(it) }
            } catch (e: IOException) {
                _historyList.value = Result.Error("Network error occurred. Please try again.")
            } catch (e: Exception) {
                _historyList.value = Result.Error("Unexpected error occurred: ${e.message}")
            }
        }
    }
}