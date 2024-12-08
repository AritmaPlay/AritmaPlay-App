package com.aritmaplay.app.ui.rank

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aritmaplay.app.data.remote.response.leaderboard.DataItem
import com.aritmaplay.app.databinding.ItemRankBinding

class AllTimeAdapter: ListAdapter<DataItem, AllTimeAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val rank = getItem(position)
        holder.bind(rank)
    }

    class MyViewHolder(private val binding: ItemRankBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(rank: DataItem) {
            binding.tvNumberListRank.text = rank.rank.toString()
            binding.tvNameRank.text = rank.name?.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase() else char.toString()
            } ?: ""
            binding.tvExpRank.text = "${rank.totalExp} EXP"

        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DataItem> =
            object : DiffUtil.ItemCallback<DataItem>() {
                override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                    return oldItem.rank == newItem.rank
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                    return oldItem == newItem
                }
            }
    }
}