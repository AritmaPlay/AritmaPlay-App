package com.aritmaplay.app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.user.ProfileResponse
import com.aritmaplay.app.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profileResult = MutableLiveData<Result<ProfileResponse>>()
    val profileResult: LiveData<Result<ProfileResponse>> = _profileResult

    fun getProfile(token: String, userId: Int) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = profileRepository.getProfile(token, userId)
            if (response.isSuccessful && response.body()?.success == true) {
                emit(Result.Success(response.body()!!))
            } else {
                emit(Result.Error(response.body()?.message ?: "Gagal memuat data profil"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Terjadi kesalahan"))
        }
    }
}