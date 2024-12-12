package com.aritmaplay.app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.error.ErrorResponse
import com.aritmaplay.app.data.remote.response.user.ProfileResponse
import com.aritmaplay.app.repository.ProfileRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profileResult = MutableLiveData<Result<ProfileResponse>>()
    val profileResult: LiveData<Result<ProfileResponse>> = _profileResult

    fun getProfile(token: String, userId: Int) {
        viewModelScope.launch {
            _profileResult.value = Result.Loading
            try {
                val response = profileRepository.getProfile(token, userId)
                _profileResult.value = Result.Success(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _profileResult.value = errorMessage?.let { Result.Error(it) }
            } catch (e: IOException) {
                _profileResult.value = Result.Error("Network error occurred. Please try again.")
            } catch (e: Exception) {
                _profileResult.value = Result.Error("Unexpected error occurred: ${e.message}")
            }
        }
    }
}