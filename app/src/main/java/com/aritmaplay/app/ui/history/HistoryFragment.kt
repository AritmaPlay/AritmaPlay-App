package com.aritmaplay.app.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aritmaplay.app.R
import com.aritmaplay.app.SpaceItemDecoration
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.data.remote.response.quiz.QuizHistoryItem
import com.aritmaplay.app.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreference = UserPreference.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    viewModel.getHistory("Bearer ${user.token}", user.userId)
                } else {
                    Toast.makeText(context, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.space_between_items)
        binding.rvHistory.addItemDecoration(SpaceItemDecoration(spaceInPixels))
        val adapter = HistoryAdapter()
        binding.rvHistory.adapter = adapter

        Log.d("HistoryFragment", "onViewCreated")

        viewModel.historyList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("HistoryFragment", "Loading")
                    binding.progressBar2.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.GONE
                    binding.tvNoDataHistory.visibility = View.GONE
                }

                is Result.Success -> {
                    Log.d("HistoryFragment", "Success")
                    val quizHistory: List<QuizHistoryItem> = result.data.data
                    if (quizHistory.isEmpty()) {
                        binding.rvHistory.visibility = View.GONE
                        binding.tvNoDataHistory.visibility = View.VISIBLE
                    } else {
                        binding.rvHistory.visibility = View.VISIBLE
                        binding.tvNoDataHistory.visibility = View.GONE
                        adapter.submitList(quizHistory)
                    }
                    binding.progressBar2.visibility = View.GONE
                }

                is Result.Error -> {
                    Log.e("RankFragment", "Error state: ${result.message}")
                    binding.progressBar2.visibility = View.GONE
                    binding.rvHistory.visibility = View.GONE
                    binding.tvNoDataHistory.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}