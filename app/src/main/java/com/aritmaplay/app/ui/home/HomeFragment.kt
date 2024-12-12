package com.aritmaplay.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aritmaplay.app.R
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.Result
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
            navigateToQuiz("Penjumlahan")
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        val userPreference = UserPreference.getInstance(requireContext().dataStore)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userPreference.getSession().collect { user ->
                    if (user.isLogin) {
                        val index = user.name.indexOf(' ')
                        if (index == -1) {
                            binding.tvWelcome.text = "Selamat Datang, ${user.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}!"
                        } else {
                            val firstName = index.let { user.name.substring(0, it) }.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                            binding.tvWelcome.text = "Selamat Datang, $firstName!"
                        }
                        viewModel.getProfile("Bearer ${user.token}", user.userId)
                    } else {
                        Toast.makeText(context, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.profileResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvPoints.visibility = View.GONE
                    binding.tvNumberRank.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                    binding.tvPoints.visibility = View.GONE
                    binding.tvNumberRank.visibility = View.GONE
                    binding.ivExp.visibility = View.GONE
                    binding.tvExpPoints.visibility = View.GONE
                    binding.ivPeringkat.visibility = View.GONE
                    binding.tvRank.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvPoints.visibility = View.VISIBLE
                    binding.tvNumberRank.visibility = View.VISIBLE
                    binding.ivExp.visibility = View.VISIBLE
                    binding.tvExpPoints.visibility = View.VISIBLE
                    binding.ivPeringkat.visibility = View.VISIBLE
                    binding.tvRank.visibility = View.VISIBLE
                    val data = result.data.data
                    binding.tvPoints.text = data?.user?.totalExp.toString()
                    binding.tvNumberRank.text = data?.user?.userRank?.toString() ?: getString(R.string.list_number_rank)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvPoints.visibility = View.GONE
                    binding.tvNumberRank.visibility = View.GONE
                    binding.ivExp.visibility = View.GONE
                    binding.tvExpPoints.visibility = View.GONE
                    binding.ivPeringkat.visibility = View.GONE
                    binding.tvRank.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT).show()
                }
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
