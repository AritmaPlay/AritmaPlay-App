package com.aritmaplay.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.local.pref.UserModel
import com.aritmaplay.app.data.remote.response.error.ErrorResponse
import com.aritmaplay.app.repository.UserRepository
import com.aritmaplay.app.data.remote.response.user.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            try {
                val response = repository.login(email, password)
                _loginResult.value = Result.Success(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _loginResult.value = errorMessage?.let { Result.Error(it) }
            } catch (e: IOException) {
                _loginResult.value = Result.Error("Network error occurred. Please try again.")
            } catch (e: Exception) {
                _loginResult.value = Result.Error("Unexpected error occurred: ${e.message}")
            }
        }
    }
}