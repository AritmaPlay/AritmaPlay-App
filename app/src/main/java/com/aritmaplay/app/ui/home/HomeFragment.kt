package com.aritmaplay.app.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aritmaplay.app.R
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.databinding.FragmentHomeBinding
import com.aritmaplay.app.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreference = UserPreference.getInstance(requireContext().dataStore)

        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    fetchProfile(user.token, user.userId)
                } else {
                    Toast.makeText(context, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchProfile(token: String, userId: Int) {
        viewModel.getProfile("Bearer $token", userId).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data.data
                    val index = data?.user?.name?.indexOf(' ')
                    if (index == -1) {
                        binding.tvWelcome.text = "Selamat Datang, ${data.user.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}!"
                    } else {
                        val firstName = index?.let { data.user.name.substring(0, it) }.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                        binding.tvWelcome.text = "Selamat Datang, $firstName!"
                    }
                }

                is Result.Error -> {
                    Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
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