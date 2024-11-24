package com.aritmaplay.app.ui.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RankViewModel : ViewModel() {

    private val _rankList = MutableLiveData<List<RankItem>>()
    val rankList: LiveData<List<RankItem>> = _rankList

    init {
        _rankList.value = listOf(
            RankItem("Alice", 150),
            RankItem("Bob", 200),
            RankItem("Charlie", 120),
            RankItem("Diana", 180),
            RankItem("Eve", 170)
        ).sortedByDescending { it.xp }
    }
}
