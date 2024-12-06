package com.aritmaplay.app.ui.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.leaderboard.LeaderboardResponse
import com.aritmaplay.app.repository.LeaderboardRepository
import kotlinx.coroutines.launch

class RankViewModel (private val repository: LeaderboardRepository) : ViewModel() {

    private val _leaderboardList = MutableLiveData<Result<LeaderboardResponse>>()
    val leaderboardList: LiveData<Result<LeaderboardResponse>> = _leaderboardList


    fun getLeaderboard(token: String) {
        viewModelScope.launch {
            _leaderboardList.value = Result.Loading
            try {
                val response = repository.getLeaderboard(token)
                _leaderboardList.value = Result.Success(response)
            } catch (e: Exception) {
                _leaderboardList.value = Result.Error(e.toString())
            }
        }
    }
}