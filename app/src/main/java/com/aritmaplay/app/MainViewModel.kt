package com.aritmaplay.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.repository.UserRepository
import com.aritmaplay.app.data.local.pref.UserModel
import com.aritmaplay.app.data.remote.response.user.ProfileResponse
import com.aritmaplay.app.repository.ProfileRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profileResult = MutableLiveData<Result<ProfileResponse>>()
    val profileResult: LiveData<Result<ProfileResponse>> = _profileResult

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun getProfile(token: String, userId: Int) {
        _profileResult.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = profileRepository.getProfile(token, userId)
                if (response.isSuccessful && response.body() != null) {
                    val userProfile = response.body()!!.data
                    _profileResult.value = Result.Success(response.body()!!)

                    userProfile?.let {
                        val user = UserModel(
                            token = token,
                            isLogin = true,
                            userId = userId,
                        )
                        userRepository.saveSession(user)
                    }
                } else {
                    _profileResult.value = Result.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _profileResult.value = Result.Error(e.toString())
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}