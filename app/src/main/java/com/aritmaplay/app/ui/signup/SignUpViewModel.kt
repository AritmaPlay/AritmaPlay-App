package com.aritmaplay.app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.error.ErrorResponse
import com.aritmaplay.app.repository.UserRepository
import com.aritmaplay.app.data.remote.response.user.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _signupResult = MutableLiveData<Result<RegisterResponse>>()
    val signupResult: LiveData<Result<RegisterResponse>> = _signupResult

    fun register(username: String, name: String, email: String, password: String) {
        viewModelScope.launch {
            _signupResult.value = Result.Loading
            try {
                val response = userRepository.register(username, name, email, password)
                _signupResult.value = Result.Success(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _signupResult.value = errorMessage?.let { Result.Error(it) }
            } catch (e: IOException) {
                _signupResult.value = Result.Error("Network error occurred. Please try again.")
            } catch (e: Exception) {
                _signupResult.value = Result.Error("Unexpected error occurred: ${e.message}")
            }
        }
    }
}