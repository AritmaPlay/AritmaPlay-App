package com.aritmaplay.app.ui.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aritmaplay.app.R
import com.aritmaplay.app.SpaceItemDecoration
import com.aritmaplay.app.databinding.FragmentRankBinding
import com.aritmaplay.app.ui.history.HistoryAdapter

class RankFragment : Fragment() {

    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(RankViewModel::class.java)

        binding.rvRank.layoutManager = LinearLayoutManager(requireContext())
        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.space_between_items)
        binding.rvRank.addItemDecoration(SpaceItemDecoration(spaceInPixels))
        val adapter = HistoryAdapter()
        binding.rvRank.adapter = adapter

        viewModel.rankList.observe(viewLifecycleOwner) { rankList ->
            if (rankList != null) {
                setupRecyclerView(rankList)
            }
        }
    }

    private fun setupRecyclerView(rankList: List<RankItem>) {
        val adapter = RankAdapter(rankList)
        binding.rvRank.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRank.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
