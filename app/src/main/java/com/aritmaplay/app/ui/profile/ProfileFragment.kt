package com.aritmaplay.app.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.aritmaplay.app.R
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.databinding.FragmentProfileBinding
import com.aritmaplay.app.ui.onboarding.OnBoardingActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)

        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    fetchProfile(user.token, user.userId)
                } else {
                    Toast.makeText(context, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                userPreference.logout()
                startActivity(Intent(requireContext(), OnBoardingActivity::class.java))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchProfile(token: String, userId: Int) {
        viewModel.getProfile("Bearer $token", userId).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvUserName.visibility = View.INVISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = result.data.data
                    binding.tvUserName.visibility = View.VISIBLE
                    binding.tvUserName.text = data?.user?.username ?: "Unknown"
                    binding.tvLevel.text = "Level: ${data?.user?.level?.toString() ?: "0"}"
                    binding.experience.text = "Exp: ${data?.user?.totalExp?.toString() ?: "0"}"
                    Log.d("ProfileFragment", "fetchProfile: ${data?.stats}")

                    val sumRate = data?.stats?.quizPenjumlahanSuccessRate ?: 0
                    binding.tvSumRate.text = "$sumRate%"
                    binding.progressBarSum.progress = sumRate

                    val subtractRate = data?.stats?.quizPenguranganSuccessRate ?: 0
                    binding.tvSubtractRate.text = "$subtractRate%"
                    binding.progressBarSubtract.progress = subtractRate

                    val multiplyRate = data?.stats?.quizPerkalianSuccessRate ?: 0
                    binding.multiplyRate.text = "$multiplyRate%"
                    binding.progressBarMultiply.progress = multiplyRate

                    val divideRate = data?.stats?.quizPembagianSuccessRate ?: 0
                    binding.tvDivideRate.text = "$divideRate%"
                    binding.progressBarDivide.progress = divideRate

                    Glide.with(requireContext())
                        .load(data?.user?.urlProfile)
                        .circleCrop()
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .error(R.drawable.ic_baseline_person_24)
                        .into(binding.profileImageView)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error Loading Data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}