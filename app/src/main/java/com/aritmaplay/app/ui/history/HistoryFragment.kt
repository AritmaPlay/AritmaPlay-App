package com.aritmaplay.app.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aritmaplay.app.R
import com.aritmaplay.app.SpaceItemDecoration
import com.aritmaplay.app.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

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

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.space_between_items)
        binding.rvHistory.addItemDecoration(SpaceItemDecoration(spaceInPixels))
        val adapter = HistoryAdapter()
        binding.rvHistory.adapter = adapter

        val viewModel = HistoryViewModel()
        viewModel.historyList.observe(viewLifecycleOwner) { data ->
            if (data != null && data.isNotEmpty()) {
                adapter.submitList(data)
            } else {
                Log.w("HistoryFragment", "No history data available")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
