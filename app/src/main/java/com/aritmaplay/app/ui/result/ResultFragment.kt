package com.aritmaplay.app.ui.result

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aritmaplay.app.R
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.databinding.FragmentResultBinding
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class ResultFragment : Fragment() {
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()

    private lateinit var finishSound: MediaPlayer

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

        playAnimation()
        finishSound = MediaPlayer.create(context, R.raw.sound_finish)

        binding.tvExpNumber.text = totalExp.toString()
        binding.tvAccuraccyNumber.text = buildString {
            append((correctAnswerCount / 10.0 * 100).toInt())
            append("%")
        }
        if (duration >= 60) {
            val minute = duration / 60
            val second = duration % 60
            binding.tvTimeNumber.text = buildString {
                append(minute)
                if (second < 10) {
                    append(":0")
                } else {
                    append(":")
                }
                append(second)
            }
        } else {
            binding.tvTimeNumber.text = buildString {
                append("0")
                if (duration < 10) {
                    append(":0")
                } else {
                    append(":")
                }
                append(duration)
            }
        }

        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(requireContext().dataStore)
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    viewModel.generateMotivation(user.name, correctAnswerCount, duration, 10, operation)
                    viewModel.result(user.token, operation, 10, duration, correctAnswerCount, user.userId)
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

        viewModel.motivation.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Result.Loading -> {
                    Log.d("ResultFragment", "Loading user data...")
                }

                is Result.Success -> {
                    binding.tvMotivation.text = state.data.data
                    binding.progressBarResult.visibility = View.GONE
                    binding.constraintLayoutResult.visibility = View.VISIBLE
                    binding.konfettiView.start(explode())
                    finishSound.start()
                }

                is Result.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    Log.e("ResultFragment", "Error: ${state.message}")
                }
            }
        }

        binding.homeButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_resultFragment_to_homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.quizFragment, true)
                    .build()
            )
        }

        binding.goButton.setOnClickListener {
            val directToQuiz = ResultFragmentDirections.actionResultFragmentToQuizFragment(operation)
            findNavController().navigate(
                directToQuiz,
                NavOptions.Builder()
                    .setPopUpTo(R.id.resultFragment, true)
                    .build()
            )
        }
    }

    fun explode(): List<Party> {
        return listOf(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                position = Position.Relative(0.5, 0.3)
            )
        )
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivResult, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}