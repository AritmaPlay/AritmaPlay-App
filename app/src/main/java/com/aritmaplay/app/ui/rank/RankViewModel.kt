package com.aritmaplay.app.ui.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.remote.response.leaderboard.AllTimeLeaderboardResponse
import com.aritmaplay.app.data.remote.response.leaderboard.WeeklyLeaderboardResponse
import com.aritmaplay.app.repository.LeaderboardRepository
import kotlinx.coroutines.launch

class RankViewModel (private val repository: LeaderboardRepository) : ViewModel() {

    private val _weeklyLeaderboardList = MutableLiveData<Result<WeeklyLeaderboardResponse>>()
    val weeklyLeaderboardList: LiveData<Result<WeeklyLeaderboardResponse>> = _weeklyLeaderboardList

    private val _allTimeLeaderboardList = MutableLiveData<Result<AllTimeLeaderboardResponse>>()
    val allTimeLeaderboardList: LiveData<Result<AllTimeLeaderboardResponse>> = _allTimeLeaderboardList

    fun getWeeklyLeaderboard(token: String) {
        viewModelScope.launch {
            _weeklyLeaderboardList.value = Result.Loading
            try {
                val response = repository.getWeeklyLeaderboard(token)
                _weeklyLeaderboardList.value = Result.Success(response)
            } catch (e: Exception) {
                _weeklyLeaderboardList.value = Result.Error(e.toString())
            }
        }
    }

    fun getAllTimeLeaderboard(token: String) {
        viewModelScope.launch {
            _allTimeLeaderboardList.value = Result.Loading
            try {
                val response = repository.getAllTimeLeaderboard(token)
                _allTimeLeaderboardList.value = Result.Success(response)
            } catch (e: Exception) {
                _allTimeLeaderboardList.value = Result.Error(e.toString())
            }
        }
    }
}