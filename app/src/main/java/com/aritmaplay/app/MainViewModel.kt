package com.aritmaplay.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.aritmaplay.app.data.local.pref.UserModel
import com.aritmaplay.app.repository.UserRepository


class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {


    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

}