package com.aritmaplay.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.repository.UserRepository
import com.aritmaplay.app.data.remote.response.user.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            try {
                val response = repository.login(email, password)
                _loginResult.value = Result.Success(response)
            } catch (e: Exception) {
                _loginResult.value = Result.Error(e.toString())
            }
        }
    }
}