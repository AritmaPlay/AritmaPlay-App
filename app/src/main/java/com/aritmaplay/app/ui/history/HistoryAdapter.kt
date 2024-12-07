package com.aritmaplay.app.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aritmaplay.app.R
import com.aritmaplay.app.data.remote.response.quiz.QuizHistoryItem
import com.aritmaplay.app.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<QuizHistoryItem, HistoryAdapter.HistoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        } else {
            Log.e("HistoryAdapter", "Item at position $position is null")
        }
    }


    class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuizHistoryItem) {
            val correctAnswers = "${item.correctQuestion}/${item.totalQuestion}"
            val expReceived  = "+${item.expReceived} EXP"

            if (item.quizTime >= 60) {
                val minute = item.quizTime / 60
                val second = item.quizTime % 60
                val time = "$minute menit $second detik"
                binding.tvQuizDuration.text = time
            } else {
                val time = "${item.quizTime} detik"
                binding.tvQuizDuration.text = time
            }

            binding.tvModeHistory.text = item.quizMode
            binding.tvJumlahHistory.text = correctAnswers
            binding.tvJumlahXp.text = expReceived

            when (item.quizMode) {
                "Penjumlahan" -> {
                    binding.ivArenaHistory.setImageResource(R.drawable.add_icon)
                }
                "Pengurangan" -> {
                    binding.ivArenaHistory.setImageResource(R.drawable.subtract_icon)
                }
                "Perkalian" -> {
                    binding.ivArenaHistory.setImageResource(R.drawable.multiply_icon)
                }
                "Pembagian" -> {
                    binding.ivArenaHistory.setImageResource(R.drawable.divine_icon)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<QuizHistoryItem>() {
        override fun areItemsTheSame(oldItem: QuizHistoryItem, newItem: QuizHistoryItem): Boolean {
            return oldItem.quizId == newItem.quizId
        }

        override fun areContentsTheSame(oldItem: QuizHistoryItem, newItem: QuizHistoryItem): Boolean {
            return oldItem == newItem
        }
    }
}