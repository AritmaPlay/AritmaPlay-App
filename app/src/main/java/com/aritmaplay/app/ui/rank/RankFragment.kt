package com.aritmaplay.app.ui.rank

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aritmaplay.app.data.Result
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aritmaplay.app.R
import com.aritmaplay.app.SpaceItemDecoration
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.databinding.FragmentRankBinding
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class RankFragment : Fragment() {
    private val viewModel by viewModels<RankViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!

    private lateinit var rankAdapter: RankAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreference = UserPreference.getInstance(requireContext().dataStore)

        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    viewModel.getLeaderboard("Bearer ${user.token}")
                } else {
                    Toast.makeText(context, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.space_between_items)
        binding.rvRank.addItemDecoration(SpaceItemDecoration(spaceInPixels))
        rankAdapter = RankAdapter()

        viewModel.leaderboardList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("RankFragment", "Loading state: Memuat data leaderboard.")
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvRank.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                    binding.tvExpRank.visibility = View.GONE
                    binding.tvNameRank.visibility = View.GONE
                }
                is Result.Success -> {
                    Log.d("RankFragment", "Success state: Data leaderboard diterima.")
                    val rankList= result.data.data?.leaderboardEntries
                    if (rankList != null) {
                        Log.d("RankFragment", "Jumlah entries diterima: ${rankList.size}")
                        if (rankList.isEmpty()) {
                            Log.d("RankFragment", "Entries kosong: Menampilkan pesan 'Tidak ada data'.")
                            binding.rvRank.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            Log.d("RankFragment", "Entries tersedia: Menampilkan RecyclerView.")
                            binding.rvRank.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.GONE
                            binding.tvExpRank.visibility = View.VISIBLE
                            binding.tvNameRank.visibility = View.VISIBLE
                            val rankedList = rankList.mapIndexed { index, item ->
                                item?.copy(rank = index + 1)
                            }
                            rankAdapter.submitList(rankedList)

                            lifecycleScope.launch {
                                val user = userPreference.getSession().firstOrNull()
                                val LoginUser = user?.let {
                                    rankedList.find { it?.userId == user.userId }
                                }
                                if (LoginUser == null) {
                                    binding.tvNumberListRank.text = getString(R.string.list_number_rank)
                                    binding.tvExpRank.visibility = View.GONE
                                    binding.tvNameRank.text = getString(R.string.first_time_user)

                                } else {
                                    binding.tvExpRank.visibility = View.VISIBLE
                                    binding.tvNumberListRank.text = LoginUser.rank.toString()
                                    binding.tvNameRank.text = LoginUser.user?.name?.let {
                                        it.replaceFirstChar { char ->
                                            if (char.isLowerCase()) char.titlecase() else char.toString()
                                        }
                                    }.orEmpty()
                                    binding.tvExpRank.text = "${LoginUser.totalExpPerWeek} EXP"
                                }
                            }
                        }
                    } else {
                        Log.w("RankFragment", "Data entries null. Tidak ada data untuk ditampilkan.")
                        binding.rvRank.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    Log.e("RankFragment", "Error state: ${result.message}")
                    binding.progressBar.visibility = View.GONE
                    binding.rvRank.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvRank.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = rankAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}