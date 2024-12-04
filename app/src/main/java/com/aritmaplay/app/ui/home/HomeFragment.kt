package com.aritmaplay.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aritmaplay.app.R
import com.aritmaplay.app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.constraintPenjumlahan.setOnClickListener {
            navigateToQuiz("Penambahan")
        }

        binding.constraintPengurangan.setOnClickListener {
            navigateToQuiz("Pengurangan")
        }

        binding.constraintPerkalian.setOnClickListener {
            navigateToQuiz("Perkalian")
        }

        binding.constraintPembagian.setOnClickListener {
            navigateToQuiz("Pembagian")
        }

        binding.historyButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }

        val textView = binding.textView2
        homeViewModel.text.observe(viewLifecycleOwner) { text ->
            textView.text = text
        }

        return root
    }

    private fun navigateToQuiz(operation: String) {
        val toQuizFragment = HomeFragmentDirections.actionHomeFragmentToQuizFragment(operation)
        findNavController().navigate(toQuizFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}