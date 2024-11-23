package com.aritmaplay.app.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {
    private val _historyList = MutableLiveData<List<HistoryItem>>()
    val historyList: LiveData<List<HistoryItem>> get() = _historyList

    init {
        _historyList.value = listOf(
            HistoryItem("Mode Penjumlahan 1", "10/10", "+100XP"),
            HistoryItem("Mode Penjumlahan 2", "9/10", "+90XP"),
            HistoryItem("Mode Penjumlahan 3", "8/10", "+80XP"),
            HistoryItem("Mode Penjumlahan 4", "7/10", "+70XP"),
            HistoryItem("Mode Penjumlahan 5", "6/10", "+60XP"),
            HistoryItem("Mode Penjumlahan 6", "5/10", "+50XP"),
            HistoryItem("Mode Penjumlahan 7", "4/10", "+40XP"),
            HistoryItem("Mode Penjumlahan 8", "3/10", "+30XP"),
            HistoryItem("Mode Penjumlahan 9", "2/10", "+20XP"),
            HistoryItem("Mode Penjumlahan 10", "1/10", "+10XP"),
            HistoryItem("Mode Penjumlahan 11", "10/10", "+110XP"),
            HistoryItem("Mode Penjumlahan 12", "9/10", "+90XP"),
            HistoryItem("Mode Penjumlahan 13", "8/10", "+80XP"),
            HistoryItem("Mode Penjumlahan 14", "7/10", "+70XP"),
            HistoryItem("Mode Penjumlahan 15", "6/10", "+60XP")
        )
    }
}


data class HistoryItem(
    val mode: String,
    val jumlahSoal: String,
    val xp: String
)
