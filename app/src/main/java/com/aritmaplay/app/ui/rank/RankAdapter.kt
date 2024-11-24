package com.aritmaplay.app.ui.rank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aritmaplay.app.databinding.ItemRankBinding

class RankAdapter(private val rankList: List<RankItem>) :
    RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val binding =
            ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        val rankItem = rankList[position]
        holder.bind(rankItem, position + 1)
    }

    override fun getItemCount(): Int = rankList.size

    inner class RankViewHolder(private val binding: ItemRankBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rankItem: RankItem, rank: Int) {
            binding.tvNumberListRank.text = rank.toString()
            binding.tvNameRank.text = rankItem.name
            binding.tvExpRank.text = "${rankItem.xp} XP"
        }
    }
}
