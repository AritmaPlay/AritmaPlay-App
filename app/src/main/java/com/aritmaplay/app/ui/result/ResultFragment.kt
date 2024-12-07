package com.aritmaplay.app.ui.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aritmaplay.app.R
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.databinding.FragmentResultBinding
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val operation = args.operation
        val correctAnswerCount = args.correctAnswerCount
        val totalExp = args.correctAnswerCount * 10
        val duration = args.duration

        binding.tvStatisticsResult.text = totalExp .toString()

        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(requireContext().dataStore)
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    viewModel.result(user.token, operation, 10, duration, correctAnswerCount)
                }
            }
        }

        viewModel.quizResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Result.Loading -> {
                    Log.d("ResultFragment", "Loading user data...")
                }

                is Result.Success -> {
                    Log.d("ResultFragment", "Success: User data posted successfully!")
                    Log.d("ResultFragment", "Data yang dikirim: ${state.data.data?.quiz}")
                }

                is Result.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    Log.e("ResultFragment", "Error: ${state.message}")
                }
            }
        }

        binding.homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
        }

        binding.goButton.setOnClickListener {
            val directToQuiz = ResultFragmentDirections.actionResultFragmentToQuizFragment(operation)
            findNavController().navigate(directToQuiz)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}