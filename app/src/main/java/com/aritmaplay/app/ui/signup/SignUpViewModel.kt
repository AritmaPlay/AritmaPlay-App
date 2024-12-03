package com.aritmaplay.app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.repository.UserRepository
import com.aritmaplay.app.data.remote.response.user.RegisterResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _signupResult = MutableLiveData<Result<RegisterResponse>>()
    val signupResult: LiveData<Result<RegisterResponse>> = _signupResult

    fun register(username: String, name: String, email: String, password: String) {
        viewModelScope.launch {
            _signupResult.value = Result.Loading
            try {
                val response = userRepository.register(username, name, email, password)
                _signupResult.value = Result.Success(response)
            } catch (e: Exception) {
                _signupResult.value = Result.Error(e.toString())
            }
        }
    }
}