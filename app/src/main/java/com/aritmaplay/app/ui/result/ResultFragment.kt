package com.aritmaplay.app.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aritmaplay.app.R
import com.aritmaplay.app.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val operation = args.operation
        val correctAnswerCount = args.correctAnswerCount * 10

        binding.tvStatisticsResult.text = correctAnswerCount.toString()

        binding.homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
        }

        binding.goButton.setOnClickListener {
            val directToQuiz = ResultFragmentDirections.actionResultFragmentToQuizFragment(operation)
            findNavController().navigate(directToQuiz)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}